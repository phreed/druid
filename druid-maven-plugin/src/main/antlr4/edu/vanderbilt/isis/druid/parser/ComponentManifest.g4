/**
 * This grammar is used to describe sets of components which work together.
 * The compoent has an identifier and a set of parts of 
 * which the component is constituted. 
 */
grammar ComponentManifest;

prog : statement+ ; 

statement 
   : buildComponent+
   | COMMENT
   | NEWLINE
   ;

buildComponent : 'build-component' ID 'of-parts' partsList ;

partsList : '[' partDecl+ ']' ;

partDecl 
   : simpleTemplate 
   | multiTemplate 
   | simpleCopy 
   ; 

simpleTemplate : 'with' 'template' FILE_PATH ; 
multiTemplate : 'for-each' subset 'with' 'template' FILE_PATH ; 
simpleCopy : 'with' 'copy-of' FILE_PATH 'to-path' TEMPLATE ; 

subset
   : 'relation'
   | 'message'
   ;

WS :   [ \t\r\n]+  -> skip ;
NEWLINE : '\r'? '\n' ;

/**
keywords
*/

COMMENT           :   '{' .*? '}' ;
ID          :   [a-zA-Z]+ ;
FILE_PATH   :   '"' .*? '"'  ;
TEMPLATE   :   '\'' .*? '\''  ;


