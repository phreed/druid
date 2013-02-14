/**
 * This grammar is used to describe sets of components which work together.
 * The compoent has an identifier and a set of parts of 
 * which the component is comprised.. 
 * Each of the parts has a rule describing how it is to be constructed.
 * 
 */
grammar ComponentManifest;

prog : statement+ ; 

statement 
   : buildComponent+
   | COMMENT
   | NEWLINE
   ;

buildComponent : 'build-component' ID 'of-parts' partsList ;

partsList : '[' partConstructionRule+ ']' ;

partConstructionRule 
   : simpleTemplate 
   | multiTemplate 
   | simpleCopy 
   ; 

simpleTemplate : 'make' fileOfType 'using' 'template' FILE_PATH ; 
multiTemplate :  'for-each' subset 'make' fileOfType 'using' 'template' FILE_PATH ; 
simpleCopy : 'make' fileOfType 'using' 'copy-of' FILE_PATH 'to-path' TEMPLATE ; 
multiCopy : 'for-each' subset 'make' fileOfType 'using' 'copy-of' FILE_PATH 'to-path' TEMPLATE ; 

fileOfType
   : BASE         # FileOfBaseType
   | SKELETON     # FileOfSkeletonType
   ;

subset
   : RELATION
   | MESSAGE
   ;

BASE :  'base' ;
SKELETON :  'skeleton' ;

RELATION :  'relation' ;
MESSAGE :  'message' ;

WS :   [ \t\r\n]+  -> skip ;
NEWLINE : '\r'? '\n' ;

/**
keywords
*/

COMMENT           :   '{' .*? '}' ;
ID          :   [a-zA-Z][a-zA-Z\-_0-9]* ;
FILE_PATH   :   '"' .*? '"'  ;
TEMPLATE   :   '\'' .*? '\''  ;


