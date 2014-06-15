What is Rialms ?
=================

Rialms is a Radical Interactive Adaptive Learning Management System. Rialms is a platform for delivering interactive and [adaptive learning](http://en.wikipedia.org/wiki/Adaptive_learning) content.

> **Adaptive learning** is an educational method which uses computers as interactive teaching devices.

Rialms adapts the presentation of learning material according to students' learning needs, as indicated by their responses to questions.

How it Works ?
===============

Rialms is based on [Question and Test Interoperability specification (QTI)](http://en.wikipedia.org/wiki/QTI) produced by [IMS Global](http://www.imsglobal.org/).
 
QTI specification is a widely used standard for marking up questions and other learning material. 
Rialms is a delivery engine that can play QTI compatible content. All learning material in this site (Questions and Tests) are created as per QTI Spec.
So Rialms can deliver more learning material by simply adding more content based on QTI Spec.

Technology
===========

Riamls is build using these popular technologies and open source libraries.

* [QTI Tools](http://qtitools.org/) library for processing QTI v2.1 questions and tests.
* [Grails](http://grails.org/) web application framework.
* [AngularJS](http://angularjs.org/) javascript framework.
* [jQuery](http://jquery.com//) javascript library.
* [Twitter Bootstrap](http://twitter.github.com/bootstrap/) front-end toolkit.
* [CoffeeScript](http://coffeescript.org/) language that transcompiles to javascript.
* [JsPlumb](http://jsplumb.org/) library to connect elements on webpage.

Getting Started
===============

* Download and install [Groovy 2.x](http://groovy.codehaus.org/Download)
* Download and install [Grails 2.1.0](https://grails.org/downloads)
* `cd /path/to/rialms` and run `grails run-app` to start application.