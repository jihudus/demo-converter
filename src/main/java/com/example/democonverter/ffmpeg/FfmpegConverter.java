package com.example.democonverter.ffmpeg;

import com.example.democonverter.files.FileStorageConfig;
import com.example.democonverter.service.ConversionService;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FfmpegConverter implements ConversionService {

    private final FFprobe ffprobe;

    private final FFmpegExecutor executor;

    private final FileStorageConfig fileStorageConfig;

    @Override
    public Runnable convert(String name, int newWidth, int newHeight) throws IOException {

        FFmpegProbeResult probeResult = ffprobe.probe(fileStorageConfig.getBasePath() + name);

        FFmpegOutputBuilder outputBuilder = new FFmpegOutputBuilder()
                .setVideoWidth(newWidth)
                .setVideoHeight(newHeight);

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(probeResult)
                .setFormat("mp4")
                .addOutput(outputBuilder)
                .addOutput(fileStorageConfig.getTempPath() + name)
                .done();

        return executor.createJob(builder);
    }


}
