package com.example.jp.model;

/**
 * Factory class to create appropriate FileItem subclass based on file extension
 */
public class FileItemFactory {

    /**
     * Create a FileItem instance of the appropriate type based on file extension
     */
    public static FileItem createFileItem(String name, String path, Long size, String mimeType) {
        String extension = extractExtension(name);
        
        if (isDocumentFile(extension)) {
            return new DocumentFile(name, path, size, mimeType);
        } else if (isImageFile(extension)) {
            return new ImageFile(name, path, size, mimeType);
        } else if (isVideoFile(extension)) {
            return new VideoFile(name, path, size, mimeType);
        } else if (isAudioFile(extension)) {
            return new AudioFile(name, path, size, mimeType);
        } else if (isArchiveFile(extension)) {
            return new ArchiveFile(name, path, size, mimeType);
        } else {
            return new GenericFile(name, path, size, mimeType);
        }
    }

    private static String extractExtension(String filename) {
        if (filename != null && filename.contains(".")) {
            return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
        }
        return "";
    }

    private static boolean isDocumentFile(String ext) {
        return ext.matches("pdf|doc|docx|txt|rtf|odt|xls|xlsx|ppt|pptx|csv");
    }

    private static boolean isImageFile(String ext) {
        return ext.matches("jpg|jpeg|png|gif|svg|bmp|webp|ico|tiff|tif");
    }

    private static boolean isVideoFile(String ext) {
        return ext.matches("mp4|avi|mov|mkv|wmv|flv|webm|mpeg|mpg|3gp");
    }

    private static boolean isAudioFile(String ext) {
        return ext.matches("mp3|wav|flac|aac|ogg|m4a|wma|opus");
    }

    private static boolean isArchiveFile(String ext) {
        return ext.matches("zip|rar|7z|tar|gz|gzip|bz2|xz");
    }
}
