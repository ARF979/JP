package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents any other file type that doesn't fit into specific categories
 */
@NoArgsConstructor
public class GenericFile extends FileItem {

    public GenericFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
    }

    @Override
    public String getFileTypeCategory() {
        return "GENERIC";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        if (!ext.isEmpty()) {
            return ext.toUpperCase() + " File";
        }
        return "Unknown File";
    }
}
