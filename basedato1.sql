CREATE SCHEMA `basedato`;
CREATE TABLE `basedato`.`historia` (
  `idHIstoria` INT NOT NULL AUTO_INCREMENT,
  `idIdioma` INT NOT NULL,
  `titulo` VARCHAR(64) NOT NULL,
  `descripcion` VARCHAR(256) NOT NULL,
  `codigoMensajeInicio` VARCHAR(64) NULL,
  PRIMARY KEY (`idHistoria`));

CREATE TABLE `basedato`.`mensaje` (
  `idMensaje` INT NOT NULL AUTO_INCREMENT,
  `idHistoria` INT NOT NULL,
  `codigoMensaje` VARCHAR(64) NOT NULL,
  `obligatorio` BOOLEAN NOT NULL,
  `mostrarMensaje` BOOLEAN NOT NULL,
  `mensajePopUp` BOOLEAN NOT NULL,
  `codigoMensajeSiguiente` VARCHAR(64) NULL,
  PRIMARY KEY (`idMensaje`));

CREATE TABLE `basedato`.`textomensaje` (
  `idTextoMensaje` INT NOT NULL AUTO_INCREMENT,
  `idMensaje` INT NOT NULL, 
  `idIdioma` INT NOT NULL,
  `textoMensaje` VARCHAR(1024) NOT NULL,
  PRIMARY KEY (`idTextoMensaje`));

CREATE TABLE `basedato`.`comando` (
  `idComando` INT NOT NULL AUTO_INCREMENT,
  `idMensaje` INT NOT NULL, 
  `codigoComando` VARCHAR(64) NOT NULL,
  `codigoMensajeSiguiente` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`idComando`));

CREATE TABLE `basedato`.`textocomando` (
  `idTextoComando` INT NOT NULL AUTO_INCREMENT,
  `idComando` INT NOT NULL, 
  `idIdioma` INT NOT NULL,
  `textoComando` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`idTextoComando`));

CREATE TABLE `basedato`.`tarea` (
  `idTarea` INT NOT NULL AUTO_INCREMENT,
  `idMensaje` INT NOT NULL,
  `numeroTarea` INT NOT NULL,
  `detalleTarea` VARCHAR(128) NOT NULL,
  PRIMARY KEY (`idTarea`));