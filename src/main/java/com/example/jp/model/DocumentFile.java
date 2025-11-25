package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents document files like PDF, DOC, DOCX, TXT, etc.
 */
@NoArgsConstructor
public class DocumentFile extends FileItem {
    
    private Integer pageCount;
    private Boolean isEditable;

    public DocumentFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
        this.isEditable = isEditableFormat();
    }

    @Override
    public String getFileTypeCategory() {
        return "DOCUMENT";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        return switch (ext) {
            case "pdf" -> "PDF Document";
            case "doc", "docx" -> "Word Document";
            case "txt" -> "Text Document";
            case "rtf" -> "Rich Text Document";
            case "odt" -> "OpenDocument Text";
            default -> "Document File";
        };
    }

    private boolean isEditableFormat() {
        String ext = getExtension();
        return ext.equals("txt") || ext.equals("doc") || ext.equals("docx") || ext.equals("rtf");
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Boolean getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Boolean isEditable) {
        this.isEditable = isEditable;
    }
}
