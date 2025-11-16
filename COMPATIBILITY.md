# Akrofolis - Paper & Folia Kompatibilität

## ✅ Vollständige Kompatibilität

**Akrofolis** ist vollständig kompatibel mit:

- ✅ **Paper 1.21.x** (1.21.0 - 1.21.10)
- ✅ **Folia 1.21.x** (1.21.0 - 1.21.10)

Das Plugin erkennt automatisch, ob es auf Paper oder Folia läuft und verwendet die entsprechenden APIs.

## Technische Details

### Scheduler-Abstraktionsschicht

Das Plugin verwendet einen **SchedulerWrapper**, der automatisch zwischen Paper und Folia wechselt:

```java
// Automatische Erkennung beim Start
private static final boolean IS_FOLIA;
static {
    boolean folia = false;
    try {
        Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
        folia = true;
    } catch (ClassNotFoundException ignored) {}
    IS_FOLIA = folia;
}
```

### Auf Paper:
- Verwendet den klassischen `BukkitScheduler`
- Alle Tasks laufen im Main-Thread
- Kompatibel mit allen Paper-basierten Servern

### Auf Folia:
- Verwendet Folia's regionbasierte Scheduler:
  - **Entity Scheduler** - Für spielerbezogene Tasks
  - **Region Scheduler** - Für ortsbezogene Tasks
  - **Global Scheduler** - Für globale Tasks
  - **Async Scheduler** - Für asynchrone Tasks
- Thread-per-Region Architektur wird vollständig unterstützt
- Keine `UnsupportedOperationException` Fehler mehr

## Migration von Bukkit.getScheduler()

Alle kritischen Module wurden migriert:

### ✅ Migrierte Module (14+):

1. **NametagManager** - Global Timer mit SchedulerWrapper
2. **ScoreboardManager** - Global Timer + Async Tasks
3. **TablistManager** - Global Timer + Delayed Tasks
4. **AutoBroadcast** - Global Timer für Broadcasts
5. **BossBarBroadcast** - Global Timer mit BossBar-Updates
6. **LobbySpawn** - Global Delayed Task
7. **WorldProtect** - Entity-basierte Delayed Tasks
8. **SongPlayerManager** - Async Delayed Tasks
9. **PlayerListener** - Entity-basierte Tasks
10. **AkropolisPlugin** - Globale Task-Verwaltung
11. **LobbyCommand** - Entity-basierte Teleport-Tasks
12. **DoubleJump** - Entity-basierte Flight-Tasks
13. **FightModeManager** - Entity-basierte Timer mit ScheduledTask
14. **HologramManager** - Global Region Tasks

### API-Version

Das Plugin verwendet `api-version: "1.21"` in der `paper-plugin.yml`, was automatisch alle Minor-Versionen von 1.21.x unterstützt:

- ✅ 1.21.0
- ✅ 1.21.1
- ✅ 1.21.2
- ✅ 1.21.3
- ✅ 1.21.4
- ✅ 1.21.5
- ✅ 1.21.6
- ✅ 1.21.7
- ✅ 1.21.8
- ✅ 1.21.9
- ✅ 1.21.10

## Folia-Spezifische Anpassungen

### Entfernte Inkompatibilitäten:

1. **Scoreboard.getNewScoreboard()** - Nicht unterstützt in Folia
   - Lösung: Automatisches Cleanup, kein manuelles Reset mehr nötig

2. **Initial Delay = 0** - Nicht erlaubt in Folia
   - Lösung: Alle `0L` Delays auf `1L` geändert

3. **Synchrone Repeating Tasks** - Nicht direkt unterstützt
   - Lösung: Entity- oder Global-basierte Timer je nach Kontext

## Testing

### Auf Paper starten:
```bash
java -jar paper-1.21.8.jar
```
Im Log sollte stehen: `Running on: Paper`

### Auf Folia starten:
```bash
java -jar folia-1.21.8.jar
```
Im Log sollte stehen: `Running on: Folia`

## Performance

### Paper:
- Traditionelles Single-Thread-Modell
- Alle Chunks auf einem Thread
- Bewährte Performance

### Folia:
- Thread-per-Region Modell
- Deutlich bessere Performance bei vielen Spielern
- Skaliert mit CPU-Kernen
- Ideal für große Hub-Server

## Support

Bei Problemen:

1. Prüfe die Logs auf `UnsupportedOperationException`
2. Stelle sicher, dass das Plugin für 1.21.x gebaut wurde
3. Verifiziere, dass keine alten `Bukkit.getScheduler()` Aufrufe mehr existieren

## Credits

- **Original DeluxeHub:** ItsLewizzz
- **Akrofolis Fork:** ZetaStormy
- **Folia-Kompatibilität:** Implementiert mit SchedulerWrapper-Pattern

---

**Version:** 1.9.3+
**Letztes Update:** 16. November 2025
**Status:** ✅ Production Ready

