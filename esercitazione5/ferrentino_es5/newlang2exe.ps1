# leggo l'input dall'utente
param(
    [String]$srcFile
)
$cwd = [String](Get-Location)
$jarpath = "out\artifacts\ferrentino_es5_jar\ferrentino_es5.jar"

# costruisco gli absolute path
$srcFile = (Join-Path $cwd $srcFile) | Resolve-Path
$srcFile = $srcFile.replace("\", "/") # normalizzo i separatori

# compilo il file newlang in .c
Start-Process "java.exe" "-jar $jarpath $srcFile" -NoNewWindow -Wait

# costruisco il percorso per il file .c
$dstCFile = $srcFile
$dstCFile = $dstCFile.Replace("test_files", "test_files/c_out").Replace(".txt", ".c")

# costruisco il percorso per il file .exe
$dstExeFile = $srcFile
$dstExeFile = $dstExeFile.Replace("test_files", "test_files/exe_out").Replace(".txt", ".exe")

# compilo il file .c in .exe
Start-Process "gcc.exe" "$dstCFile -o $dstExeFile" -NoNewWindow -Wait
