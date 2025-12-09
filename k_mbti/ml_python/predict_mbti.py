import sys
import os
import io
import joblib
import warnings

# 경고 메시지 무시 (Java 로그 오염 방지)
warnings.filterwarnings("ignore")

# ✅ 핵심: 한글 깨짐 방지 및 입출력 설정
sys.stdin = io.TextIOWrapper(sys.stdin.detach(), encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.detach(), encoding='utf-8')

def load_model():
    # 모델 파일 경로를 '현재 실행 파일' 기준으로 잡음 (경로 에러 해결)
    base_path = os.path.dirname(os.path.abspath(__file__))
    model_path = os.path.join(base_path, 'mbti_model.joblib')
    vectorizer_path = os.path.join(base_path, 'mbti_vectorizer.joblib')

    if not os.path.exists(model_path) or not os.path.exists(vectorizer_path):
        print(f"ERROR:Model files not found at {base_path}")
        sys.exit(1)

    model = joblib.load(model_path)
    vectorizer = joblib.load(vectorizer_path)
    return model, vectorizer

def predict():
    try:
        # ✅ 핵심: Java에서 보내주는 데이터를 스트림으로 통째로 읽음 (길이 제한 없음)
        input_text = sys.stdin.read().strip()

        if not input_text:
            print("ERROR:Empty Input")
            return

        model, vectorizer = load_model()

        # 예측
        text_vector = vectorizer.transform([input_text])
        prediction = model.predict(text_vector)[0]
        probability = model.predict_proba(text_vector).max()

        # 결과 출력
        print(f"{prediction},{probability:.3f}")

    except Exception as e:
        print(f"ERROR:{str(e)}")

if __name__ == '__main__':
    predict()