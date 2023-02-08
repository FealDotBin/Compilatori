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
float sommac(int a, int d, float b, char** size);
void esempio();
void stampa(char* messaggio);
int c = 1;


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

float sommac(int a, int d, float b, char** size){
float result;


result = a+b+c+d;
if(result>100){
char* valore;

valore = "grande";

*size = valore;
} else {
char* valore;

valore = "piccola";

*size = valore;
}
return result;
}

void main(){


esempio();
}

void esempio(){
int a;
float b;
int x;

char* taglia;
char* ans1;

char* ans;

float risultato;

a = 1;
b = 2.2;
x = 3;
ans = "no";
risultato = sommac(a, x, b, &taglia)
;

stampa(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings("la somma di ", convert_int_to_string(a)), " e "), convert_real_to_string(b)), " incrementata di "), convert_int_to_string(c)), " è "), taglia));
stampa(concat_strings("ed è pari a ", convert_real_to_string(risultato)));
printf("%s", "vuoi continuare? (si/no) - inserisci due volte la risposta");
printf("\n");
read_string(&ans);
read_string(&ans1);
while(strcmp(ans,"si") == 0){

printf("%s", "inserisci un intero:");
read_int(&a);
printf("%s", "inserisci un reale:");
read_float(&b);
risultato = sommac(a, c, b, &taglia)
;
stampa(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings(concat_strings("la somma di ", convert_int_to_string(a)), " e "), convert_real_to_string(b)), " incrementata di "), convert_int_to_string(c)), " è "), taglia));
stampa(concat_strings(" ed è pari a ", convert_real_to_string(risultato)));
printf("%s", "vuoi continuare? (si/no):\t");
read_string(&ans);
}
printf("%s", "");
printf("\n");
printf("%s", "ciao");
}

void stampa(char* messaggio){

for(int i = 1; i <= 4; i++){

printf("%s", "");
printf("\n");
}
printf("%s", messaggio);
}


