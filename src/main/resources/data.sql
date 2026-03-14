-- Seed Users (passwords are BCrypt of "password123")
INSERT INTO users (id, username, email, password, role, created_at) VALUES
(1, 'alice',   'alice@example.com',   '$2a$10$7QJ8G1z5K3mN9pL2vX4wOOuQYtR6hD0sA1bC3eF5gH7iJ9kL0mN2', 'USER',  CURRENT_TIMESTAMP),
(2, 'bob',     'bob@example.com',     '$2a$10$7QJ8G1z5K3mN9pL2vX4wOOuQYtR6hD0sA1bC3eF5gH7iJ9kL0mN2', 'USER',  CURRENT_TIMESTAMP),
(3, 'charlie', 'charlie@example.com', '$2a$10$7QJ8G1z5K3mN9pL2vX4wOOuQYtR6hD0sA1bC3eF5gH7iJ9kL0mN2', 'USER',  CURRENT_TIMESTAMP),
(4, 'admin',   'admin@example.com',   '$2a$10$7QJ8G1z5K3mN9pL2vX4wOOuQYtR6hD0sA1bC3eF5gH7iJ9kL0mN2', 'ADMIN', CURRENT_TIMESTAMP);

-- Seed Ideas
INSERT INTO ideas (id, title, description, status, author_id, created_at, updated_at) VALUES
(1, 'Dark Mode for Dashboard',    'Add a dark mode toggle to the main dashboard for better UX at night.', 'OPEN',   1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Slack Integration',          'Integrate notifications with Slack so teams stay updated in real-time.',  'OPEN',   2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Export Reports to PDF',      'Allow users to export any report as a downloadable PDF file.',           'OPEN',   3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Mobile App',                 'Build a companion mobile application for iOS and Android.',               'OPEN',   1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Two-Factor Authentication',  'Add 2FA support via TOTP apps like Google Authenticator.',              'CLOSED', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Seed Votes
INSERT INTO votes (id, user_id, idea_id, vote_type, created_at) VALUES
(1, 1, 2, 'UPVOTE',   CURRENT_TIMESTAMP),
(2, 2, 1, 'UPVOTE',   CURRENT_TIMESTAMP),
(3, 3, 1, 'UPVOTE',   CURRENT_TIMESTAMP),
(4, 4, 3, 'UPVOTE',   CURRENT_TIMESTAMP),
(5, 1, 4, 'DOWNVOTE', CURRENT_TIMESTAMP),
(6, 3, 5, 'UPVOTE',   CURRENT_TIMESTAMP);