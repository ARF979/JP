package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents compressed archive files like ZIP, RAR, TAR, etc.
 */
@NoArgsConstructor
public class ArchiveFile extends FileItem {
    
    private Integer fileCount;
    private Long uncompressedSize;
    private String compressionMethod;
    private Boolean isEncrypted;

    public ArchiveFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
        this.isEncrypted = false;
    }

    @Override
    public String getFileTypeCategory() {
        return "ARCHIVE";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        return switch (ext) {
            case "zip" -> "ZIP Archive";
            case "rar" -> "RAR Archive";
            case "7z" -> "7-Zip Archive";
            case "tar" -> "TAR Archive";
            case "gz", "gzip" -> "GZip Archive";
            case "bz2" -> "BZip2 Archive";
            default -> "Archive File";
        };
    }

    public String getCompressionRatio() {
        if (uncompressedSize != null && getSize() != null && uncompressedSize > 0) {
            double ratio = (1.0 - (double) getSize() / uncompressedSize) * 100;
            return String.format("%.1f%%", ratio);
        }
        return "Unknown";
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Long getUncompressedSize() {
        return uncompressedSize;
    }

    public void setUncompressedSize(Long uncompressedSize) {
        this.uncompressedSize = uncompressedSize;
    }

    public String getCompressionMethod() {
        return compressionMethod;
    }

    public void setCompressionMethod(String compressionMethod) {
        this.compressionMethod = compressionMethod;
    }

    public Boolean getIsEncrypted() {
        return isEncrypted;
    }

    public void setIsEncrypted(Boolean isEncrypted) {
        this.isEncrypted = isEncrypted;
    }
}
