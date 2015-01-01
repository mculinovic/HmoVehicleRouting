<html>

<head>
	<style type="text/css">
	  #container {
	    max-width: 1500px;
	    height: 1200px;
	    margin: auto;
	  }
	</style>
</head>

<body>


<?php
	function randomColor() { 
	    $possibilities = array(1, 2, 3, 4, 5, 6, 7, 8, 9, "A", "B", "C", "D", "E", "F" );
		shuffle($possibilities);
		$color = "#";
		for($i = 1; $i <= 6; $i++){
			$color .= $possibilities[rand(0,14)];
		}
		return $color;
	} 


	$node = array();
	$nodes = array();

	$edge = array();
	$edgess = array();

	$handle = fopen("ulaz.txt", "r");
	$noUsers = 0;
	$noStorage = 0;

	if ($handle) {
		$noUsers = intval(trim(fgets($handle)));
		$noStorage = intval(trim(fgets($handle)));

		fgets($handle);
		for ($i=1; $i <= $noStorage; $i++) {
			$line = trim(fgets($handle));
			$coordinates = explode("\t", $line);

			$node['id'] = "s".$i;
			$node['label'] = "s".$i;
			$node['x'] = intval($coordinates[0]);
			$node['y'] = intval($coordinates[1]);
			$node['color'] = "#0000FF";
			$node['size'] = 4;
 
			array_push($nodes, $node);
		}

		fgets($handle);
		for ($i=0; $i < $noUsers; $i++) {
			$line = trim(fgets($handle));
			$coordinates = explode("\t", $line);

			$node['id'] = "u".$i;
			$node['label'] = "u".$i;
			$node['x'] = intval($coordinates[0]);
			$node['y'] = intval($coordinates[1]);
			$node['color'] = "#F80000";
			$node['size'] = 2;

			array_push($nodes, $node);
		}
	}
	else {
	  header("Location: err.php?e=Greška kod učitavanja datoteke!");
	} 
	fclose($handle);


	$handle = fopen("izlaz.txt", "r");
	$noCycle = 0;
	$edgeID = 1;

	if ($handle) {
		$noCycle = intval(fgets($handle));

		for ($i = 1; $i <= $noCycle; $i++) {
			fgets($handle);
			$line = trim(fgets($handle));
			$line = str_replace(":  ", " ", $line);
			$lineParts = explode(" ", $line);
			$colorCode = randomColor();

			for ($j = 0; $j < count($lineParts)-1; $j++) {
				$edge['id'] = "e".$edgeID;
				if ($j == 0)
					$edge['source'] = "s".$lineParts[$j];
				else 
					$edge['source'] = "u".$lineParts[$j];
				$edge['target'] = "u".$lineParts[$j+1];
				$edge['color'] = $colorCode;

				array_push($edgess, $edge);
				$edgeID++;
			}
		}
	}
	else {
	  header("Location: err.php?e=Greška kod učitavanja datoteke!");
	} 
	fclose($handle);


	$named_array = array(
	    "nodes" => $nodes,
	    "edges" => $edgess
	);

	file_put_contents('data.json', print_r(json_encode($named_array, JSON_PRETTY_PRINT), true));

?>

	<div id="container"></div>

	<script src="plugins/sigma.min.js"></script>
	<script src="plugins/sigma.parsers.json.min.js"></script>

	<script>
		sigma.parsers.json('data.json', {
			container: 'container',
			settings: {
				defaultNodeColor: '#ec5148'
			}
		});
	</script>

</body>
</html>