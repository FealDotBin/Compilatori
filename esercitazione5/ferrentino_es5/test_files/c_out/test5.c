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
void mainFun();
void fun1(char* a, char** b);
void fun2(char* a, char** b);
void fun11(char* a, char** b);
void fun22(char** a, char* b);


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


mainFun();
}

void mainFun(){
char* a;

char* b;


printf("%s", "[i] Passo a per valore e b per riferimento. Fun1 passerà a per valore e b per riferimento.");
printf("\n");
a = "a";
b = "b";
printf("%s", "[i] mainFun: Stampo i valori di a e b prima della chiamata a fun1");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", b);
printf("\n");
fun1(a, &b);
printf("%s", "[i] mainFun: Stampo i valori di a e b dopo la chiamata a fun1");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", b);
printf("\n");
printf("%s", "==============================================================================================");
printf("\n");
printf("%s", "[i] Passo a per valore e b per riferimento. Fun11 passerà a per riferimento e b per valore.");
printf("\n");
a = "a";
b = "b";
printf("%s", "[i] mainFun: Stampo i valori di a e b prima della chiamata a fun11");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", b);
printf("\n");
fun11(a, &b);
printf("%s", "[i] mainFun: Stampo i valori di a e b dopo la chiamata a fun11");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", b);
printf("\n");
}

void fun1(char* a, char** b){

a = concat_strings(a, "a");
*b = concat_strings(*b, "b");
printf("%s", "[i] fun1: Stampo i valori di a e b prima della chiamata a fun2");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", *b);
printf("\n");
fun2(a, &*b);
printf("%s", "[i] fun1: Stampo i valori di a e b dopo la chiamata a fun2");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", *b);
printf("\n");
}

void fun2(char* a, char** b){

a = concat_strings(a, "a");
*b = concat_strings(*b, "b");
printf("%s", "[i] fun2: Stampo i valori di a e b");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", *b);
printf("\n");
}

void fun11(char* a, char** b){

a = concat_strings(a, "a");
*b = concat_strings(*b, "b");
printf("%s", "[i] fun11: Stampo i valori di a e b prima della chiamata a fun22");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", *b);
printf("\n");
fun22(&a, *b);
printf("%s", "[i] fun11: Stampo i valori di a e b dopo la chiamata a fun22");
printf("\n");
printf("%s", "a: ");
printf("%s", a);
printf("\n");
printf("%s", "b: ");
printf("%s", *b);
printf("\n");
}

void fun22(char** a, char* b){

*a = concat_strings(*a, "a");
b = concat_strings(b, "b");
printf("%s", "[i] fun22: Stampo i valori di a e b");
printf("\n");
printf("%s", "a: ");
printf("%s", *a);
printf("\n");
printf("%s", "b: ");
printf("%s", b);
printf("\n");
}


