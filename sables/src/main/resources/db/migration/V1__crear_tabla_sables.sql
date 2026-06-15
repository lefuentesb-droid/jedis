CREATE TABLE sables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    color VARCHAR(20) NOT NULL,
    cristal_kyber VARCHAR(30) NOT NULL,
    jedi_id INT NOT NULL
);

INSERT INTO sables (color, cristal_kyber, jedi_id) 
VALUES ('Verde', 'Ilum', 1);