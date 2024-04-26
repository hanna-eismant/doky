ALTER TABLE user ADD CONSTRAINT uid_chk_nonempty CHECK (uid <> '');

ALTER TABLE reset_password_token ADD CONSTRAINT token_chk_nonempty CHECK (token <> '');

ALTER TABLE document ADD CONSTRAINT name_chk_nonempty CHECK (name <> '');
