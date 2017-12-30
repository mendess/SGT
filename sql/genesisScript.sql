-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema SGT
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema SGT
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `SGT` DEFAULT CHARACTER SET utf8 ;
USE `SGT` ;

-- -----------------------------------------------------
-- Table `SGT`.`Utilizador`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Utilizador` (
  `id` VARCHAR(6) NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  `pass` VARCHAR(255) NOT NULL,
  `Email` VARCHAR(255) NOT NULL,
  `loginAtivo` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Aluno` (
  `Utilizador_id` VARCHAR(6) NOT NULL,
  `eEspecial` TINYINT(1) NOT NULL,
  PRIMARY KEY (`Utilizador_id`),
  INDEX `fk_Aluno_Utilizador1_idx` (`Utilizador_id` ASC),
  CONSTRAINT `fk_Aluno_Utilizador1`
    FOREIGN KEY (`Utilizador_id`)
    REFERENCES `SGT`.`Utilizador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Docente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Docente` (
  `Utilizador_id` VARCHAR(6) NOT NULL,
  PRIMARY KEY (`Utilizador_id`),
  INDEX `fk_Docente_Utilizador_idx` (`Utilizador_id` ASC),
  CONSTRAINT `fk_Docente_Utilizador`
    FOREIGN KEY (`Utilizador_id`)
    REFERENCES `SGT`.`Utilizador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`UC`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`UC` (
  `id` VARCHAR(6) NOT NULL,
  `nome` VARCHAR(255) NOT NULL,
  `acron` VARCHAR(255) NOT NULL,
  `responsavel_id` VARCHAR(6) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_UC_Docente1_idx` (`responsavel_id` ASC),
  CONSTRAINT `fk_UC_Docente1`
    FOREIGN KEY (`responsavel_id`)
    REFERENCES `SGT`.`Docente` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Turno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Turno` (
  `id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `Docente_id` VARCHAR(6) NULL,
  `vagas` INT(11) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `UC_id`, `ePratico`),
  INDEX `fk_Turno_UC1_idx` (`UC_id` ASC),
  INDEX `fk_Turno_Docente1_idx` (`Docente_id` ASC),
  CONSTRAINT `fk_Turno_UC1`
    FOREIGN KEY (`UC_id`)
    REFERENCES `SGT`.`UC` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Turno_Docente1`
    FOREIGN KEY (`Docente_id`)
    REFERENCES `SGT`.`Docente` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Aula`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Aula` (
  `id` INT(11) NOT NULL,
  `Turno_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `Turno_id`, `UC_id`, `ePratico`),
  INDEX `fk_Aula_Turno1_idx` (`Turno_id` ASC, `UC_id` ASC, `ePratico` ASC),
  CONSTRAINT `fk_Aula_Turno1`
    FOREIGN KEY (`Turno_id` , `UC_id` , `ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`DiaSemana`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`DiaSemana` (
  `dia` CHAR(3) NOT NULL,
  PRIMARY KEY (`dia`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`DiretorDeCurso`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`DiretorDeCurso` (
  `Utilizador_id` VARCHAR(6) NOT NULL,
  `trocasPermitidas` TINYINT(1) NOT NULL DEFAULT 1,
  PRIMARY KEY (`Utilizador_id`),
  INDEX `fk_DiretorDeCurso_Utilizador1_idx` (`Utilizador_id` ASC),
  CONSTRAINT `fk_DiretorDeCurso_Utilizador1`
    FOREIGN KEY (`Utilizador_id`)
    REFERENCES `SGT`.`Utilizador` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Trocas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Trocas` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dataRealizacao` DATETIME NOT NULL,
  `aluno_id` VARCHAR(6) NOT NULL,
  `turnoOrigem_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `turnoOrigem_ePratico` TINYINT(1) NOT NULL,
  `turnoDestino_id` INT(11) NOT NULL,
  `UC_id1` VARCHAR(6) NOT NULL,
  `turnoDestino_ePratico` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Trocas_Aluno1_idx` (`aluno_id` ASC),
  INDEX `fk_Trocas_Turno1_idx` (`turnoOrigem_id` ASC, `UC_id` ASC, `turnoOrigem_ePratico` ASC),
  INDEX `fk_Trocas_Turno2_idx` (`turnoDestino_id` ASC, `UC_id1` ASC, `turnoDestino_ePratico` ASC),
  CONSTRAINT `fk_Trocas_Aluno1`
    FOREIGN KEY (`aluno_id`)
    REFERENCES `SGT`.`Aluno` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Trocas_Turno1`
    FOREIGN KEY (`turnoOrigem_id` , `UC_id` , `turnoOrigem_ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Trocas_Turno2`
    FOREIGN KEY (`turnoDestino_id` , `UC_id1` , `turnoDestino_ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`TurnoInfo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`TurnoInfo` (
  `id` INT(11) NOT NULL,
  `dia_id` CHAR(3) NOT NULL,
  `horaInicio` TIME NOT NULL,
  `horaFim` TIME NOT NULL,
  `Turno_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`, `dia_id`, `Turno_id`, `UC_id`, `ePratico`),
  INDEX `fk_TurnoInfo_DiaSemana1_idx` (`dia_id` ASC),
  INDEX `fk_TurnoInfo_Turno1_idx` (`Turno_id` ASC, `UC_id` ASC, `ePratico` ASC),
  CONSTRAINT `fk_TurnoInfo_DiaSemana1`
    FOREIGN KEY (`dia_id`)
    REFERENCES `SGT`.`DiaSemana` (`dia`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TurnoInfo_Turno1`
    FOREIGN KEY (`Turno_id` , `UC_id` , `ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Presencas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Presencas` (
  `Aluno_id` VARCHAR(6) NOT NULL,
  `Aula_id` INT(11) NOT NULL,
  `Turno_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  PRIMARY KEY (`Aluno_id`, `Aula_id`, `Turno_id`, `UC_id`, `ePratico`),
  INDEX `fk_Aula_has_Aluno_Aluno1_idx` (`Aluno_id` ASC),
  INDEX `fk_Presencas_Aula1_idx` (`Aula_id` ASC, `Turno_id` ASC, `UC_id` ASC, `ePratico` ASC),
  CONSTRAINT `fk_Aula_has_Aluno_Aluno1`
    FOREIGN KEY (`Aluno_id`)
    REFERENCES `SGT`.`Aluno` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Presencas_Aula1`
    FOREIGN KEY (`Aula_id` , `Turno_id` , `UC_id` , `ePratico`)
    REFERENCES `SGT`.`Aula` (`id` , `Turno_id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Turno_has_Aluno`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Turno_has_Aluno` (
  `Turno_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  `Aluno_id` VARCHAR(6) NOT NULL,
  PRIMARY KEY (`Turno_id`, `UC_id`, `ePratico`, `Aluno_id`),
  INDEX `fk_Turno_has_Aluno_Aluno1_idx` (`Aluno_id` ASC),
  INDEX `fk_Turno_has_Aluno_Turno1_idx` (`Turno_id` ASC, `UC_id` ASC, `ePratico` ASC),
  CONSTRAINT `fk_Turno_has_Aluno_Turno1`
    FOREIGN KEY (`Turno_id` , `UC_id` , `ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Turno_has_Aluno_Aluno1`
    FOREIGN KEY (`Aluno_id`)
    REFERENCES `SGT`.`Aluno` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `SGT`.`Pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `SGT`.`Pedido` (
  `Turno_id` INT(11) NOT NULL,
  `UC_id` VARCHAR(6) NOT NULL,
  `ePratico` TINYINT(1) NOT NULL,
  `Aluno_id` VARCHAR(6) NOT NULL,
  PRIMARY KEY (`Turno_id`, `UC_id`, `ePratico`, `Aluno_id`),
  INDEX `fk_Turno_has_Aluno1_Aluno1_idx` (`Aluno_id` ASC),
  INDEX `fk_Turno_has_Aluno1_Turno1_idx` (`Turno_id` ASC, `UC_id` ASC, `ePratico` ASC),
  CONSTRAINT `fk_Turno_has_Aluno1_Turno1`
    FOREIGN KEY (`Turno_id` , `UC_id` , `ePratico`)
    REFERENCES `SGT`.`Turno` (`id` , `UC_id` , `ePratico`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Turno_has_Aluno1_Aluno1`
    FOREIGN KEY (`Aluno_id`)
    REFERENCES `SGT`.`Aluno` (`Utilizador_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE USER	'aplicacao'@'localhost'
  IDENTIFIED BY 'ap123';
GRANT SELECT, INSERT, UPDATE, DELETE ON SGT.* TO 'aplicacao'@'localhost';

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
