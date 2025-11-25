-- Test data for testing environment
-- Test Users
INSERT INTO users (id, name, password, role, created_at, updated_at) VALUES 
(1, 'testuser1', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(2, 'testuser2', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'USER', '2024-01-01 10:00:00', '2024-01-01 10:00:00'),
(3, 'testadmin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'ADMIN', '2024-01-01 10:00:00', '2024-01-01 10:00:00');

-- Test Vocabularies
INSERT INTO vocabulary (id, title, description, user_id, created_at, updated_at) VALUES 
(1, 'Test Vocabulary 1', 'Test description for vocabulary 1', 1, '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(2, 'Test Vocabulary 2', 'Test description for vocabulary 2', 1, '2024-01-01 11:00:00', '2024-01-01 11:00:00'),
(3, 'Test Vocabulary 3', 'Test description for vocabulary 3', 2, '2024-01-01 11:00:00', '2024-01-01 11:00:00');

-- Test Words
INSERT INTO word (id, english_word, korean_meaning, vocabulary_id, created_at, updated_at) VALUES 
(1, 'test', '테스트', 1, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(2, 'example', '예시', 1, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(3, 'sample', '샘플', 1, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(4, 'hello', '안녕하세요', 2, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(5, 'world', '세계', 2, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(6, 'computer', '컴퓨터', 3, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(7, 'programming', '프로그래밍', 3, '2024-01-01 12:00:00', '2024-01-01 12:00:00'),
(8, 'database', '데이터베이스', 3, '2024-01-01 12:00:00', '2024-01-01 12:00:00');