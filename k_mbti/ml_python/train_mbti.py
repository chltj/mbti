import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import classification_report, accuracy_score
import joblib

# ✅ 1. 데이터 로드
data_path = "../src/main/java/com/example/k_mbti/mbti/ml/data/mbti_raw.csv"
df = pd.read_csv(data_path)

print("✅ 데이터 로드 완료:", df.shape)

# ✅ 2. 입력(X), 정답(y) 분리
X = df["text"]
y = df["mbti"]

# ✅ 3. 학습/검증 데이터 분리
X_train, X_test, y_train, y_test = train_test_split(
    X, y, test_size=0.2, random_state=42, stratify=y
)

print("✅ 학습 데이터:", X_train.shape)
print("✅ 검증 데이터:", X_test.shape)

# ✅ 4. TF-IDF 벡터화
vectorizer = TfidfVectorizer(
    max_features=5000,
    ngram_range=(1, 2)
)

X_train_vec = vectorizer.fit_transform(X_train)
X_test_vec = vectorizer.transform(X_test)

print("✅ TF-IDF 벡터화 완료")

# ✅ 5. 분류기 학습 (Logistic Regression)
model = LogisticRegression(
    max_iter=2000,
    n_jobs=-1,
    multi_class="auto"
)

model.fit(X_train_vec, y_train)

print("✅ 모델 학습 완료")

# ✅ 6. 예측 및 성능 평가
y_pred = model.predict(X_test_vec)
acc = accuracy_score(y_test, y_pred)

print("\n==============================")
print("✅ 모델 정확도:", acc)
print("==============================\n")

print("✅ 분류 리포트:")
print(classification_report(y_test, y_pred))

# ✅ 7. 모델 저장
joblib.dump(model, "mbti_model.joblib")
joblib.dump(vectorizer, "mbti_vectorizer.joblib")

print("\n✅ 모델 저장 완료:")
print(" - mbti_model.joblib")
print(" - mbti_vectorizer.joblib")
