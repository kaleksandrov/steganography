# Steganography

## Description

This is a web application written in java programming language that implements LSB steganography algorithm. It uses JSP and Servlets technologies for managing the backend and jQuery for the AJAX requests. The communication between frontend and backend is performed using REST services. The application processes PNG images.

## Build

Just run ```mvn clean install``` from the root directory

## Deploy

1. Create ```/tmp/steganography``` folder and make sure it is writbale.
1. Start a tomcat server. 
1. Copy ```target/steganography.war``` file to ```tomcat/webapps``` folder

## Screenshot

![screenshot](https://raw.github.com/kaleksandrov/steganography/master/screenshot.png)
