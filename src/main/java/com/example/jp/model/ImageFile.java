package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents image files like JPG, PNG, GIF, SVG, etc.
 */
@NoArgsConstructor
public class ImageFile extends FileItem {
    
    private Integer width;
    private Integer height;
    private String colorSpace;
    private Boolean hasTransparency;

    public ImageFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
        this.hasTransparency = supportsTransparency();
    }

    @Override
    public String getFileTypeCategory() {
        return "IMAGE";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        return switch (ext) {
            case "jpg", "jpeg" -> "JPEG Image";
            case "png" -> "PNG Image";
            case "gif" -> "GIF Image";
            case "svg" -> "SVG Vector Image";
            case "bmp" -> "Bitmap Image";
            case "webp" -> "WebP Image";
            case "ico" -> "Icon Image";
            default -> "Image File";
        };
    }

    private boolean supportsTransparency() {
        String ext = getExtension();
        return ext.equals("png") || ext.equals("gif") || ext.equals("svg") || ext.equals("webp");
    }

    public String getDimensions() {
        if (width != null && height != null) {
            return width + "x" + height;
        }
        return "Unknown";
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getColorSpace() {
        return colorSpace;
    }

    public void setColorSpace(String colorSpace) {
        this.colorSpace = colorSpace;
    }

    public Boolean getHasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(Boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }
}
