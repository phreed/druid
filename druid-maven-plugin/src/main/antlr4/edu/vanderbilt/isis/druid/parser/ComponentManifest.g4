grammar ComponentManifest;

WS :   (' '|'\t'|'\r'? '\n')  -> skip ;
OPEN_PAREN  :   '(' ;
CLOSE_PAREN :   ')' ;
OPEN_BRACKET      :   '[' ;
CLOSE_BRACKET     :   ']' ;
SEMI_COLON     :   ';' ;

/**
keywords
*/

COMMENT           :   'comment' ;
BUILD_COMPONENT   :   'build-component' ;
OF_PARTS          :   'of-parts' ;
NAME        :   TextChar+ ;
WITH        :   'with' ;
TEMPLATE_KW :   'template' ;
COPY_OF     :   'copy-of' ;
FOR_EACH    :   'for-each' ;
TO_PATH     :   'to-path' ;
RELATION    :   'relation' ;
FILE_PATH   :   '"' TextChar* '"'  ;
TEMPLATE   :   '"' TextChar* '"'  ;

fragment
TextChar        :   ~[(]+ ;        // match any 16 bit char other than ( and &

comment : COMMENT ;

buildComponent : BUILD_COMPONENT NAME OF_PARTS partsList ;

partsList : OPEN_BRACKET (partDecl SEMI_COLON)+ CLOSE_BRACKET ;

partDecl 
   : singleTemplate 
   | multiTemplate 
   | singleCopy 
   ; 

singleTemplate : WITH TEMPLATE_KW FILE_PATH ; 
multiTemplate : FOR_EACH NAME WITH TEMPLATE_KW FILE_PATH ; 
singleCopy : WITH COPY_OF FILE_PATH TO_PATH TEMPLATE ; 
