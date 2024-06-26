INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('admin', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'test1@gmail.com', 'superadmin', null, null, true, 0);
--12345678
INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('test2', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'test2@gmail.com', 'admin', null, null, true, 0);
--12345678
INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('test3', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'test3@gmail.com', 'user', CURRENT_TIMESTAMP, null, true, 4);
--12345678
INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('test4', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'test4@gmail.com', 'user', null, null, false, null);
--12345678

INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('test5', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'test5@gmail.com', 'user', CURRENT_TIMESTAMP, 6789, true, 0);

INSERT INTO users(login, password, email, role, date_connexion, code_pin, status, accessFail)
VALUES ('testDeactive', '1000:f3c39fd0ae89721e0d0cd00544f198ec:919b258506edd3f88af44c35249c2a005c296b52039b69419badf0a3961a8797a4b67454e6fe3e60f66f2f8b6825c4bbb785094198f24f2d6f73dfc423b8f341', 'testDeactive@gmail.com', 'user', null, null, false, null);