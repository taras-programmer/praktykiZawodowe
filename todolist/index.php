<?php
$dbFile = __DIR__ . '/todolist.db';
$bd = new PDO('sqlite:' . $dbFile);
    $bd->exec("CREATE TABLE IF NOT EXISTS listaZadan (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        zadanie TEXT NOT NULL,
        statusWykonania TEXT DEFAULT('ToDo'),
        dataStworzenia DATETIME DEFAULT (datetime('now')),
        dataWykonania DATETIME
    )");
if ($_SERVER['REQUEST_METHOD'] === 'POST' && !empty($_POST['tekstZadania'])) {
    $tekstZadania = $_POST['tekstZadania'];
    $tekstZadania = $bd->quote($tekstZadania);
    $bd->exec("INSERT INTO listaZadan (zadanie) VALUES ($tekstZadania)");
    header("Location: index.php"); 
    exit;
}

if (isset($_GET['akcja']) && isset($_GET['id'])) {
    $id = (int)$_GET['id'];

    if ($_GET['akcja'] === 'wykonane') {
        $bd->exec("UPDATE listaZadan SET statusWykonania='Wykonane', dataWykonania=datetime('now') WHERE id=$id");
    } elseif ($_GET['akcja'] === 'niewykonane') {
        $bd->exec("UPDATE listaZadan SET statusWykonania='Niewykonane', dataWykonania=NULL WHERE id=$id");
    }elseif($_GET['akcja'] === 'usun'){
      $bd->exec("DELETE FROM listaZadan WHERE id=$id");
    }

    header("Location: index.php");
    exit;
}
 $zadania = $bd->query('SELECT id, zadanie, statusWykonania, dataStworzenia, dataWykonania FROM listaZadan ORDER BY id DESC LIMIT 50')->fetchAll(PDO::FETCH_ASSOC);

?>

<!DOCTYPE html>
<html lang="pl">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ToDoList</title>
</head>
<body>
    <form action="index.php" method="post">
        Podaj zadanie: <input type="text" name="tekstZadania">
        <button type="submit">Wyślij</button>
    </form>
    <table border="1" cellpadding="6" cellspacing="0">
    <tr><th>ID</th><th>Zadanie</th><th>Status</th><th>Data Stworzenia</th><th>Zmien status</th><th>Data Wykonania(nie wykonania)</th><th>usunięcie zadania</th></tr>
    <?php 
    foreach($zadania as $r): ?>
      <tr>
        <td><?=$r['id']?></td>
        <td><?=$r['zadanie']?></td>
        <td><?=$r['statusWykonania']?></td>
        <td><?=$r['dataStworzenia']?></td>
        <td>
          <a href="index.php?akcja=wykonane&id=<?=$r['id']?>">Wykonane</a>  |
          <a href="index.php?akcja=niewykonane&id=<?=$r['id']?>">Niewykonane</a>
        </td>
        <td><?=$r['dataWykonania']?></td>
        <td><a href="index.php?akcja=usun&id=<?=$r['id']?>">Usuń</a></td>
      </tr>
    <?php endforeach; ?>
  </table>
</body>
</html>