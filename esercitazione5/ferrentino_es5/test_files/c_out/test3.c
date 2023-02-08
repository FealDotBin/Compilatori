#include <stdio.h>
#include <stdlib.h>
#include <string.h> 
#include <malloc.h> 
#include <math.h> 
#include <errno.h> 
#include <limits.h> 
#include <stdbool.h> 

char* concat_strings(char* string1, char* string2);
void read_int(int *num); 
void read_float(float *num); 
void read_bool(bool *b); 
void read_char(char *c); 
void read_string(char **string);
char* convert_int_to_string(int num); 
char* convert_real_to_string(float num); 
char* convert_char_to_string(char c); 
char* convert_bool_to_string(bool b); 
void mainFunction();
void printVar(int intero, float reale, char* stringa, bool booleano, char carattere);
void modifyVar(int* intero, float* reale, char** stringa, bool* booleano, char* carattere);
void useVarBeforeDecl();
int retIntFun(int* a);
char* HEY = "HEY";
int VOTO_COMPILATORI = 18;


char* concat_strings(char* string1, char* string2){
    size_t len = strlen(string1) + strlen(string2);
    char * buffer = (char *)malloc(len + 1);
    buffer[0] = '\0';
    buffer = strncat(buffer, string1, strlen(string1));
    buffer = strncat(buffer, string2, strlen(string2));
    return buffer;
}

void read_int(int *num){
    long a; 
    char *buf = ""; 
    read_string(&buf); 
    errno = 0; 
    char *endptr; 
    a = strtol(buf, &endptr, 10); 
    if (errno == ERANGE){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if (endptr == buf){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if (*endptr && *endptr != '\n'){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if (a > INT_MAX || a < INT_MIN){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    *num = (int) a; 
}

void read_float(float *num){ 
    float a; 
    char *buf = ""; 
    read_string(&buf); 
    errno = 0; 
    char *endptr; 
    a = strtof(buf, &endptr); 
    if (errno == ERANGE){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if (endptr == buf){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if (*endptr && *endptr != '\n'){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    *num = (float) a; 
} 

void read_bool(bool *b){ 
    char *buf = ""; 
    read_string(&buf); 
    if(strcmp(buf, "true") == 0){ 
        *b = true; 
        return; 
    } 
    if(strcmp(buf, "false") == 0){ 
        *b = false; 
        return; 
    } 
    fprintf(stderr, "[!] Invalid Input\n");} 

void read_char(char *c){ 
    char *buf = ""; 
    read_string(&buf); 
    if(strlen(buf) != 1){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    *c = buf[0]; 
} 

void read_string(char **string){ 
    int ch; 
    char buffer[4096]; 
    if (fgets(buffer, 4096, stdin) == NULL){ 
        fprintf(stderr, "[!] Invalid Input\n"); 
        return; 
    } 
    if(strstr(buffer, "\n") == NULL){ 
        while((ch = getchar()) != '\n' && ch != EOF); 
        buffer[4095] = '\0'; 
    } else { 
        buffer[strcspn(buffer, "\n")] = '\0'; 
    } 
    *string = strndup(buffer, strlen(buffer)); 
} 

char* convert_int_to_string(int num){ 
    char* str = (char *) malloc(sizeof(char) * 20); 
    snprintf(str, sizeof str, "%d", num); 
    return str; 
} 

char* convert_real_to_string(float num){ 
    char* str = (char *) malloc(sizeof(char) * 20); 
    snprintf(str, sizeof str, "%f", num); 
    return str; 
} 

char* convert_char_to_string(char c){ 
    char* str = (char *) malloc(sizeof(char) * 2); 
    str[0] = c; 
    str[1] = '\0'; 
    return str; 
} 

char* convert_bool_to_string(bool b){ 
    if(b) { 
        return "true"; 
    } else { 
        return "false"; 
    } 
} 

void main(){


mainFunction();
}

void mainFunction(){
int intero;
float reale;
char* stringa;
bool booleano;
char carattere;

intero = 3;
reale = 3.3;
stringa = "hello";
booleano = true;
carattere = 'a';

printVar(retIntFun(&intero)
, reale, stringa, booleano, carattere);
modifyVar(&intero, &reale, &stringa, &booleano, &carattere);
printVar(intero, reale, stringa, booleano, carattere);
printf("%s", "Inserisci (sottomettendo con INVIO ogni scelta) i valori per integer, real, string, boolean, char: \n");
read_int(&intero);
read_float(&reale);
read_string(&stringa);
read_bool(&booleano);
read_char(&carattere);
printVar(intero, reale, stringa, booleano, carattere);
if(true){
int intero;

intero = 9999;

printf("%s", "[i] La variabile \"intero\" fa shadowing");
printf("%s", "\n");
printf("%d", intero);
printf("\n");
}
printf("%s", "[i] Stampo \"intero\" fuori dal blocco if-then");
printf("%s", "\n");
printf("%d", intero);
printf("\n");
printf("%s", "[i] Stampo la variabile globale \"HEY\" ");
printf("%s", "\n");
printf("%s", HEY);
printf("\n");
printf("%s", "[i] Stampo la variabile globale \"VOTO_COMPILATORI\" ");
printf("%s", "\n");
printf("%d", VOTO_COMPILATORI);
printf("\n");
useVarBeforeDecl();
printf("%s", "[i] Stampo -1--1 = ");
printf("%d", (-1)-(-1));
printf("\n");
printf("%s", "[i] Stampo le escape sequence");
printf("\n");
printf("%s", "\n\t\r\"\\");
printf("\n");
retIntFun(&intero);
return;
}

void printVar(int intero, float reale, char* stringa, bool booleano, char carattere){

printf("%s", "[i] Stampa delle variabili");
printf("\n");
printf("%d", intero);
printf("\n");
printf("%f", reale);
printf("\n");
printf("%s", stringa);
printf("\n");
printf("%s", booleano ? "true" : "false");
printf("\n");
printf("%c", carattere);
printf("\n");
}

void modifyVar(int* intero, float* reale, char** stringa, bool* booleano, char* carattere){

printf("%s", "[i] Modifica delle variabili");
printf("\n");
*intero = *intero+1;
*reale = *reale+1;
*stringa = concat_strings(*stringa, "hello");
*booleano = !*booleano;
*carattere = 'b';
}

void useVarBeforeDecl(){
int a;

int b;

a = b;
b = 2;

printf("%s", "[i] Stampo \"a\", dichiarata prima di \"b\"");
printf("%s", "\n");
printf("%d", a);
printf("\n");
printf("%s", "[i] Stampo \"b\", dichiarata dopo \"a\"");
printf("%s", "\n");
printf("%d", b);
printf("\n");
}

int retIntFun(int* a){

*a = *a;
return *a;
}


