<?php
session_start();

$dbDir = __DIR__ . '/data';
if (!is_dir($dbDir)) mkdir($dbDir, 0755, true);
$dbFile = $dbDir . '/quiz.db';

try {
    $pdo = new PDO("sqlite:" . $dbFile);
    $pdo->exec("CREATE TABLE IF NOT EXISTS wyniki (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        imie TEXT NOT NULL,
        wynik INTEGER NOT NULL DEFAULT 0,
        data TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    )");
} catch (Exception $e) {
    die("Błąd bazy danych: " . htmlspecialchars($e->getMessage()));
}

$wiadomosc = '';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $imie = trim($_POST["imie"] ?? '');

    if ($imie === '') {
        $wiadomosc = "Wpisz imię!";
    } else {
        $poprawne = [
            1 => 'Phishing',
            2 => 'Hasło + dodatkowy kod',
            3 => '!Qz8$Rk1',
            4 => 'Zgłosić specjalistom',
            5 => 'Ochrona przed złośliwym oprogramowaniem'
        ];

        $wynik = 0;
        for ($i = 1; $i <= 5; $i++) {
            if (isset($_POST["p$i"]) && $_POST["p$i"] === $poprawne[$i]) {
                $wynik++;
            }
        }

        $stmt = $pdo->prepare("INSERT INTO wyniki (imie, wynik) VALUES (:imie, :wynik)");
        $stmt->execute([':imie' => $imie, ':wynik' => (int)$wynik]);

        $_SESSION['wiadomosc'] = "Twój wynik: $wynik / 5";
        header("Location: " . $_SERVER["PHP_SELF"]);
        exit;
    }
}

if (isset($_SESSION['wiadomosc'])) {
    $wiadomosc = $_SESSION['wiadomosc'];
    unset($_SESSION['wiadomosc']);
}

$wyniki = $pdo->query("SELECT imie, wynik, data FROM wyniki ORDER BY id DESC LIMIT 100")->fetchAll(PDO::FETCH_ASSOC);
?>
<!DOCTYPE html>
<html lang="pl">
<head>
<meta charset="UTF-8">
<title>Quiz Cyberbezpieczeństwa</title>
<style>
    body { font-family: Arial; padding: 20px; max-width: 900px; margin: auto; }
    .pytanie { margin: 15px 0; }
    table { border-collapse: collapse; margin-top: 30px; width: 100%; }
    th, td { padding: 8px 10px; border: 1px solid #444; text-align: center; }
    th { background: #eee; }
    .msg { padding: 10px; background: #f0f8ff; border: 1px solid #cce; display:inline-block; margin-bottom:10px; }
</style>
</head>
<body>

<h1>Quiz o cyberbezpieczeństwie</h1>

<?php if($wiadomosc): ?>
<div class="msg"><strong><?=htmlspecialchars($wiadomosc)?></strong></div>
<?php endif; ?>

<form method="post" autocomplete="off">

<label><strong>Twoje imię:</strong></label><br>
<input type="text" name="imie" required maxlength="50" style="margin-bottom:20px"><br><br>

<div class="pytanie">
<b>1) Co jest najczęstszą metodą ataków na konta?</b><br>
<label><input type="radio" name="p1" value="Phishing" required> Phishing</label><br>
<label><input type="radio" name="p1" value="Słabe hasło"> Słabe hasło</label><br>
<label><input type="radio" name="p1" value="Magia hakerów"> Magia hakerów</label><br>
<label><input type="radio" name="p1" value="Wirusy"> Wirusy</label>
</div>

<div class="pytanie">
<b>2) Co to jest uwierzytelnianie dwuskładnikowe (2FA)?</b><br>
<label><input type="radio" name="p2" value="Hasło + dodatkowy kod" required> Hasło + dodatkowy kod</label><br>
<label><input type="radio" name="p2" value="Długie hasło"> Długie hasło</label><br>
<label><input type="radio" name="p2" value="Włączenie VPN"> Włączenie VPN</label><br>
<label><input type="radio" name="p2" value="Nic z tego"> Nic z tego</label>
</div>

<div class="pytanie">
<b>3) Które hasło jest najbezpieczniejsze?</b><br>
<label><input type="radio" name="p3" value="123456" required> 123456</label><br>
<label><input type="radio" name="p3" value="Password"> Password</label><br>
<label><input type="radio" name="p3" value="!Qz8$Rk1"> !Qz8$Rk1</label><br>
<label><input type="radio" name="p3" value="Twoje imię"> Twoje imię</label>
</div>

<div class="pytanie">
<b>4) Co zrobić z podejrzanymi linkami?</b><br>
<label><input type="radio" name="p4" value="Kliknąć — może coś fajnego?" required> Kliknąć</label><br>
<label><input type="radio" name="p4" value="Ignorować"> Ignorować</label><br>
<label><input type="radio" name="p4" value="Zgłosić specjalistom"> Zgłosić specjalistom</label><br>
<label><input type="radio" name="p4" value="Usunąć wiadomość"> Usunąć wiadomość</label>
</div>

<div class="pytanie">
<b>5) Do czego służy antywirus?</b><br>
<label><input type="radio" name="p5" value="Ochrona przed złośliwym oprogramowaniem" required> Ochrona</label><br>
<label><input type="radio" name="p5" value="Żeby spowolnić komputer"> Żeby spowolnić komputer</label><br>
<label><input type="radio" name="p5" value="Ładna ikonka"> Ładna ikonka</label><br>
<label><input type="radio" name="p5" value="Do niczego"> Do niczego</label>
</div>

<br>
<button type="submit">Wyślij</button>

</form>

<h2>Wyniki uczestników</h2>

<table>
    <tr>
        <th>Imię</th>
        <th>Wynik</th>
        <th>Data</th>
    </tr>
    <?php foreach ($wyniki as $row): ?>
    <tr>
        <td><?=htmlspecialchars($row['imie'])?></td>
        <td><?=$row['wynik']?> / 5</td>
        <td><?=htmlspecialchars($row['data'])?></td>
    </tr>
    <?php endforeach; ?>
</table>
</body>
</html>
