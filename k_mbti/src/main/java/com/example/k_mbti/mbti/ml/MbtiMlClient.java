package com.example.k_mbti.mbti.ml; // 패키지명은 본인 프로젝트에 맞게 유지

import org.springframework.stereotype.Component;
import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class MbtiMlClient {

    private static final String PYTHON_PATH = "python"; // 안되면 전체 경로(ex: C:\\Python39\\python.exe)

    // ❗ 본인 PC의 파이썬 스크립트 경로로 꼭 확인해주세요!
    private static final String SCRIPT_PATH =
            "C:/project/mbti/k_mbti/ml_python/predict_mbti.py";

    public MbtiMlResult predict(String fullText) {
        
        // 텍스트 없으면 즉시 종료
        if (fullText == null || fullText.trim().isEmpty()) {
            return new MbtiMlResult("UNKNOWN", 0.0);
        }

        Process process = null;
        try {
            // 1. Python 실행 (텍스트를 인자로 넣지 않음!)
            ProcessBuilder pb = new ProcessBuilder(PYTHON_PATH, SCRIPT_PATH);
            pb.redirectErrorStream(false); 
            process = pb.start();

            // 2. Java -> Python 데이터 주입 (BufferedWriter 사용)
            // 이렇게 하면 대화 내용이 아무리 길어도 윈도우 제한에 걸리지 않음
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(fullText);
                writer.flush();
            } // 여기서 닫히면서 입력 끝(EOF) 신호 전송

            // 3. 결과 읽기
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String resultLine = reader.readLine();

            // 4. 에러 로그 읽기 (디버깅용)
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8));
            String errorLine = errorReader.readLine();

            process.waitFor();

            // 콘솔에 진행상황 출력
            if (errorLine != null) System.err.println("❌ Python Error: " + errorLine);
            System.out.println("✅ Python Result: " + resultLine);

            // 5. 결과 파싱 (더미 ESTP 제거됨)
            if (resultLine != null && resultLine.contains(",")) {
                if (resultLine.startsWith("ERROR:")) {
                    return new MbtiMlResult("UNKNOWN", 0.0);
                }
                String[] parts = resultLine.split(",");
                return new MbtiMlResult(parts[0].trim(), Double.parseDouble(parts[1].trim()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (process != null) process.destroy();
        }

        return new MbtiMlResult("UNKNOWN", 0.0);
    }
}
