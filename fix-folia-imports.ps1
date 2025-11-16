# PowerShell Script to fix Folia compatibility imports

$files = @(
    "src\main\java\me\zetastormy\akropolis\AkropolisPlugin.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\world\SongPlayerManager.java",
    "src\main\java\me\zetastormy\akropolis\inventory\AbstractInventory.java",
    "src\main\java\me\zetastormy\akropolis\command\commands\AkropolisCommand.java",
    "src\main\java\me\zetastormy\akropolis\command\commands\FlyCommand.java",
    "src\main\java\me\zetastormy\akropolis\command\commands\LobbyCommand.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\hotbar\HotbarItem.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\player\DoubleJump.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\player\PlayerListener.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\visual\scoreboard\ScoreboardManager.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\world\LobbySpawn.java",
    "src\main\java\me\zetastormy\akropolis\module\modules\world\WorldProtect.java"
)

$importToAdd = "import me.zetastormy.akropolis.util.scheduler.SchedulerWrapper;"

foreach ($file in $files) {
    $fullPath = Join-Path $PSScriptRoot $file
    if (Test-Path $fullPath) {
        $content = Get-Content $fullPath -Raw

        # Check if import already exists
        if ($content -notmatch [regex]::Escape($importToAdd)) {
            # Find the last import statement
            if ($content -match '(?s)(import\s+[^;]+;\s*)+') {
                $lastImportEnd = $Matches[0].Length + $content.IndexOf($Matches[0])
                $before = $content.Substring(0, $lastImportEnd)
                $after = $content.Substring($lastImportEnd)

                $newContent = $before + "`n" + $importToAdd + $after
                Set-Content -Path $fullPath -Value $newContent -NoNewline
                Write-Host "Updated: $file" -ForegroundColor Green
            }
        } else {
            Write-Host "Already has import: $file" -ForegroundColor Yellow
        }
    } else {
        Write-Host "File not found: $file" -ForegroundColor Red
    }
}

Write-Host "`nImport fixes completed!" -ForegroundColor Cyan

