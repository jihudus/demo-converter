package com.example.democonverter.ffmpeg;

import lombok.Getter;
import lombok.Setter;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ConfigurationProperties(prefix = "ffmpeg")
@Getter
@Setter
public class FfmpegConfig {

    private String ffmpegPath;

    private String ffprobePath;

    @Bean
    FFmpeg ffmpeg() throws IOException {
        return new FFmpeg(ffmpegPath);
    }

    @Bean
    FFprobe ffprobe() throws IOException {
        return new FFprobe(ffprobePath);
    }

    @Bean
    FFmpegExecutor executor() throws IOException {
        return new FFmpegExecutor(ffmpeg(), ffprobe());
    }
}
