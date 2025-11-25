package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents video files like MP4, AVI, MOV, etc.
 */
@NoArgsConstructor
public class VideoFile extends FileItem {
    
    private Integer durationSeconds;
    private String resolution;
    private Integer frameRate;
    private String codec;
    private Boolean hasAudio;

    public VideoFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
        this.hasAudio = true; // Default assumption
    }

    @Override
    public String getFileTypeCategory() {
        return "VIDEO";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        return switch (ext) {
            case "mp4" -> "MP4 Video";
            case "avi" -> "AVI Video";
            case "mov" -> "QuickTime Video";
            case "mkv" -> "Matroska Video";
            case "wmv" -> "Windows Media Video";
            case "flv" -> "Flash Video";
            case "webm" -> "WebM Video";
            default -> "Video File";
        };
    }

    public String getFormattedDuration() {
        if (durationSeconds == null) {
            return "Unknown";
        }
        int hours = durationSeconds / 3600;
        int minutes = (durationSeconds % 3600) / 60;
        int seconds = durationSeconds % 60;
        
        if (hours > 0) {
            return String.format("%d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%d:%02d", minutes, seconds);
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public String getCodec() {
        return codec;
    }

    public void setCodec(String codec) {
        this.codec = codec;
    }

    public Boolean getHasAudio() {
        return hasAudio;
    }

    public void setHasAudio(Boolean hasAudio) {
        this.hasAudio = hasAudio;
    }
}
