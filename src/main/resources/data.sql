-- Sample data for production/development environment
-- Users
INSERT INTO users (name, password, role, created_at, updated_at) VALUES 
('admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN', NOW(), NOW()),
('john_doe', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', NOW(), NOW()),
('jane_smith', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', NOW(), NOW()),
('mike_wilson', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', NOW(), NOW()),
('sarah_jones', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', NOW(), NOW());

-- Vocabularies
INSERT INTO vocabulary (title, description, user_id, created_at, updated_at) VALUES 
('TOEIC Essential Words', 'Essential vocabulary for TOEIC test preparation', 2, NOW(), NOW()),
('Business English', 'Common business terms and phrases', 2, NOW(), NOW()),
('Daily Conversation', 'Everyday English vocabulary', 3, NOW(), NOW()),
('Academic Vocabulary', 'Words commonly used in academic contexts', 3, NOW(), NOW()),
('Travel Phrases', 'Useful phrases for traveling abroad', 4, NOW(), NOW()),
('Technology Terms', 'IT and technology related vocabulary', 4, NOW(), NOW()),
('Medical Terminology', 'Basic medical vocabulary', 5, NOW(), NOW()),
('Cooking Vocabulary', 'Kitchen and cooking related terms', 5, NOW(), NOW());

-- Words for TOEIC Essential Words (vocabulary_id = 1)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('accomplish', '달성하다, 완수하다', 1, NOW(), NOW()),
('acquire', '획득하다, 습득하다', 1, NOW(), NOW()),
('adequate', '적절한, 충분한', 1, NOW(), NOW()),
('analyze', '분석하다', 1, NOW(), NOW()),
('annual', '연간의, 매년의', 1, NOW(), NOW()),
('anticipate', '예상하다, 기대하다', 1, NOW(), NOW()),
('appreciate', '감사하다, 이해하다', 1, NOW(), NOW()),
('appropriate', '적절한, 알맞은', 1, NOW(), NOW()),
('approximately', '대략, 약', 1, NOW(), NOW()),
('authorize', '권한을 주다, 허가하다', 1, NOW(), NOW());

-- Words for Business English (vocabulary_id = 2)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('revenue', '수익, 매출', 2, NOW(), NOW()),
('profit', '이익, 수익', 2, NOW(), NOW()),
('negotiate', '협상하다', 2, NOW(), NOW()),
('conference', '회의', 2, NOW(), NOW()),
('schedule', '일정, 계획', 2, NOW(), NOW()),
('deadline', '마감일', 2, NOW(), NOW()),
('proposal', '제안서', 2, NOW(), NOW()),
('contract', '계약', 2, NOW(), NOW()),
('budget', '예산', 2, NOW(), NOW()),
('marketing', '마케팅', 2, NOW(), NOW());

-- Words for Daily Conversation (vocabulary_id = 3)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('weather', '날씨', 3, NOW(), NOW()),
('restaurant', '식당', 3, NOW(), NOW()),
('grocery', '식료품', 3, NOW(), NOW()),
('appointment', '약속', 3, NOW(), NOW()),
('exercise', '운동', 3, NOW(), NOW()),
('neighborhood', '동네', 3, NOW(), NOW()),
('laundry', '빨래', 3, NOW(), NOW()),
('transportation', '교통', 3, NOW(), NOW()),
('shopping', '쇼핑', 3, NOW(), NOW()),
('vacation', '휴가', 3, NOW(), NOW());

-- Words for Academic Vocabulary (vocabulary_id = 4)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('research', '연구', 4, NOW(), NOW()),
('hypothesis', '가설', 4, NOW(), NOW()),
('experiment', '실험', 4, NOW(), NOW()),
('conclusion', '결론', 4, NOW(), NOW()),
('methodology', '방법론', 4, NOW(), NOW()),
('analysis', '분석', 4, NOW(), NOW()),
('evidence', '증거', 4, NOW(), NOW()),
('theory', '이론', 4, NOW(), NOW()),
('literature', '문헌', 4, NOW(), NOW()),
('criteria', '기준', 4, NOW(), NOW());

-- Words for Travel Phrases (vocabulary_id = 5)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('passport', '여권', 5, NOW(), NOW()),
('luggage', '짐', 5, NOW(), NOW()),
('reservation', '예약', 5, NOW(), NOW()),
('departure', '출발', 5, NOW(), NOW()),
('arrival', '도착', 5, NOW(), NOW()),
('currency', '통화', 5, NOW(), NOW()),
('embassy', '대사관', 5, NOW(), NOW()),
('tourist', '관광객', 5, NOW(), NOW()),
('accommodation', '숙박시설', 5, NOW(), NOW()),
('itinerary', '여행 일정', 5, NOW(), NOW());

-- Words for Technology Terms (vocabulary_id = 6)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('algorithm', '알고리즘', 6, NOW(), NOW()),
('database', '데이터베이스', 6, NOW(), NOW()),
('software', '소프트웨어', 6, NOW(), NOW()),
('hardware', '하드웨어', 6, NOW(), NOW()),
('network', '네트워크', 6, NOW(), NOW()),
('security', '보안', 6, NOW(), NOW()),
('interface', '인터페이스', 6, NOW(), NOW()),
('programming', '프로그래밍', 6, NOW(), NOW()),
('artificial intelligence', '인공지능', 6, NOW(), NOW()),
('cloud computing', '클라우드 컴퓨팅', 6, NOW(), NOW());

-- Words for Medical Terminology (vocabulary_id = 7)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('diagnosis', '진단', 7, NOW(), NOW()),
('treatment', '치료', 7, NOW(), NOW()),
('prescription', '처방전', 7, NOW(), NOW()),
('symptom', '증상', 7, NOW(), NOW()),
('medicine', '약', 7, NOW(), NOW()),
('hospital', '병원', 7, NOW(), NOW()),
('surgery', '수술', 7, NOW(), NOW()),
('patient', '환자', 7, NOW(), NOW()),
('doctor', '의사', 7, NOW(), NOW()),
('nurse', '간호사', 7, NOW(), NOW());

-- Words for Cooking Vocabulary (vocabulary_id = 8)
INSERT INTO word (english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
('ingredient', '재료', 8, NOW(), NOW()),
('recipe', '조리법', 8, NOW(), NOW()),
('seasoning', '양념', 8, NOW(), NOW()),
('utensil', '조리도구', 8, NOW(), NOW()),
('refrigerator', '냉장고', 8, NOW(), NOW()),
('oven', '오븐', 8, NOW(), NOW()),
('boil', '끓이다', 8, NOW(), NOW()),
('fry', '튀기다', 8, NOW(), NOW()),
('bake', '굽다', 8, NOW(), NOW()),
('steam', '찌다', 8, NOW(), NOW());