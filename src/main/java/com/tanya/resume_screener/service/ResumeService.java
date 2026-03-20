package com.tanya.resume_screener.service;

import com.tanya.resume_screener.model.Resume;
import com.tanya.resume_screener.repository.ResumeRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.Loader;

import java.io.IOException;
import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final GeminiService geminiService;

    public ResumeService(ResumeRepository resumeRepository, GeminiService geminiService) {
        this.resumeRepository = resumeRepository;
        this.geminiService = geminiService;
    }

    public Resume uploadAndAnalyze(MultipartFile file, String jobDescription) throws IOException {
        PDDocument document = Loader.loadPDF(file.getBytes());
        PDFTextStripper stripper = new PDFTextStripper();
        String extractedText = stripper.getText(document);
        document.close();

        String truncatedText = extractedText.length() > 3000 ? extractedText.substring(0, 3000) : extractedText;
        String aiAnalysis = geminiService.analyzeResume(truncatedText, jobDescription);
        int score = parseScore(aiAnalysis);


        Resume resume = new Resume();
        resume.setFileName(file.getOriginalFilename());
        resume.setExtractedText(extractedText);
        resume.setAiAnalysis(aiAnalysis);
        resume.setMatchScore(score);

        return resumeRepository.save(resume);
    }

    public List<Resume> getAllResumes() {
        return resumeRepository.findAll();
    }

    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
    }

    private int parseScore(String aiResponse) {
        try {
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
                    "(?i)match\\s*score[:\\s]*.*?(\\d{1,3})\\s*/\\s*100"
            );
            java.util.regex.Matcher matcher = pattern.matcher(aiResponse);
            if (matcher.find()) {
                int score = Integer.parseInt(matcher.group(1));
                if (score >= 0 && score <= 100) {
                    return score;
                }
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }
}