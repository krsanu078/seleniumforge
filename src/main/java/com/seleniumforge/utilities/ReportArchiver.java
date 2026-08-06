package com.seleniumforge.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * ReportArchiver prepares a zip archive of the generated Extent report and related artifacts.
 */
public final class ReportArchiver {

    private static final Logger LOGGER = LogManager.getLogger(ReportArchiver.class);
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ReportArchiver() {
        // utility
    }

    /**
     * Archive the given Extent HTML report and associated screenshots/metadata into a zip file.
     * The archive is placed under reports/archive/ExtentReport_<ts>.zip
     *
     * @param reportHtmlPath path to the Extent HTML report
     * @return path to the created zip archive
     * @throws Exception on IO errors
     */
    public static Path archiveReport(Path reportHtmlPath) throws Exception {
        if (reportHtmlPath == null || !Files.exists(reportHtmlPath)) {
            throw new IllegalArgumentException("Report file not found: " + reportHtmlPath);
        }
        Path reportsDir = reportHtmlPath.getParent();
        Path archiveDir = reportsDir.resolve("archive");
        if (!Files.exists(archiveDir)) Files.createDirectories(archiveDir);
        String ts = LocalDateTime.now().format(FORMAT);
        Path zipPath = archiveDir.resolve("ExtentReport_" + ts + ".zip");
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            // add html report
            addFileToZip(zos, reportHtmlPath, reportHtmlPath.getFileName().toString());
            // add screenshots dir if exists
            Path screenshots = Path.of("screenshots");
            if (Files.exists(screenshots)) {
                Files.walk(screenshots).filter(p -> !Files.isDirectory(p)).forEach(p -> {
                    try {
                        Path rel = screenshots.relativize(p);
                        addFileToZip(zos, p, "screenshots/" + rel.toString());
                    } catch (Exception e) {
                        LOGGER.warn("Failed to add screenshot to archive: {}", e.getMessage());
                    }
                });
            }
            // add metadata file if present
            Path meta = reportsDir.resolve("last-run-metadata.json");
            if (Files.exists(meta)) {
                addFileToZip(zos, meta, meta.getFileName().toString());
            }
        }
        LOGGER.info("Report archived to {}", zipPath.toAbsolutePath());
        return zipPath;
    }

    private static void addFileToZip(ZipOutputStream zos, Path filePath, String entryName) throws Exception {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(filePath.toFile()))) {
            ZipEntry entry = new ZipEntry(entryName);
            zos.putNextEntry(entry);
            byte[] buffer = new byte[4096];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                zos.write(buffer, 0, read);
            }
            zos.closeEntry();
        }
    }
}
