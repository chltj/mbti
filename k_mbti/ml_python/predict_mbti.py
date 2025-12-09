import joblib
import sys

# ✅ 저장된 모델 로드
model = joblib.load("mbti_model.joblib")
vectorizer = joblib.load("mbti_vectorizer.joblib")

# ✅ Spring에서 텍스트 입력받기
input_text = sys.argv[1]

# ✅ 벡터화
X = vectorizer.transform([input_text])

# ✅ 예측
pred = model.predict(X)[0]
proba = max(model.predict_proba(X)[0])

# ✅ 결과 출력 (Spring에서 이 문자열을 읽게 됨)
print(f"{pred},{proba}")
