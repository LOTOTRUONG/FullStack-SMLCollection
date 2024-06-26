--test for pays resources
INSERT INTO pays(libelle_pays)
VALUES ('TESTPAY1'), ('TESTPAY2'), ('TESTPAY3');

--test for type of object resources
INSERT INTO type_objet(libelle_type)
VALUES ('TestType1'), ('TestType2'), ('TestType3');

INSERT INTO users(login) VALUES ('TestLogin1'),('TestLogin2');


--test for objet resources
INSERT INTO objet(libelle_objet, id_type, id_pays, login)
VALUES ('TestObjet1', 1, 1, 'TestLogin1'), ('TestObjet2', 2, 1, 'TestLogin1'), ('TestObjet3', 3, 1, 'TestLogin1');



