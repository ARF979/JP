package com.example.jp.model;

import lombok.NoArgsConstructor;

/**
 * Represents audio files like MP3, WAV, FLAC, etc.
 */
@NoArgsConstructor
public class AudioFile extends FileItem {
    
    private Integer durationSeconds;
    private String bitrate;
    private Integer sampleRate;
    private String artist;
    private String album;
    private String genre;

    public AudioFile(String name, String path, Long size, String mimeType) {
        super(name, path, size, mimeType, null, null);
    }

    @Override
    public String getFileTypeCategory() {
        return "AUDIO";
    }

    @Override
    public String getFileTypeDescription() {
        String ext = getExtension();
        return switch (ext) {
            case "mp3" -> "MP3 Audio";
            case "wav" -> "WAV Audio";
            case "flac" -> "FLAC Audio";
            case "aac" -> "AAC Audio";
            case "ogg" -> "Ogg Vorbis Audio";
            case "m4a" -> "M4A Audio";
            case "wma" -> "Windows Media Audio";
            default -> "Audio File";
        };
    }

    public String getFormattedDuration() {
        if (durationSeconds == null) {
            return "Unknown";
        }
        int minutes = durationSeconds / 60;
        int seconds = durationSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getBitrate() {
        return bitrate;
    }

    public void setBitrate(String bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
