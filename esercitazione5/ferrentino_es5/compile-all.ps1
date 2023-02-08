$testFolder = ".\test_files"
$files = Get-ChildItem $testFolder -File

foreach ($file in $files) {
    $script = ".\newlang2exe.ps1"
    $params = ".\test_files\$file"

    Write-Host "Compiling $file..."
    Invoke-Expression -Command "$script $params" -ErrorAction Stop
    Write-Host "...done!"
}