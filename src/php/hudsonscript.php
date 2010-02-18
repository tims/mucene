<?
//this script is currently run in a screen session on webteam@hubert 
//(accessible from within the office, NOT badger etc.)

// URL to Hudson server dashboard (rendered as XML)
define("HUDSON_XML_URL", "http://hudson2.last.fm:8080/api/xml");
// URL to Ethernet-based power socket
define("NETCONTROL_URL", "http://bears.office.last.fm/ctrl.cgi");

// Power socket configuration
define("GREEN_BEAR", 0);
define("YELLOW_BEAR", 1);
define("RED_BEAR", 2);
define("SWITCH_ON", 0);
define("SWITCH_OFF", 1);

/**
 * get a file from specified url
 *
 * @param string $remote url of the file
 * @param string $local  save contents to this file
 *
 * @return boolen true on success, false on failure.
*/
function get_file($remote, $local) {
  /* get hostname and path of the remote file */
  $host = parse_url($remote, PHP_URL_HOST);
  $path = parse_url($remote, PHP_URL_PATH);
               
  /* prepare request headers */
  $reqhead = "GET $path HTTP/1.1\r\n" . "Host: $host\r\n" . "Connection: Close\r\n\r\n";
                                   
  /* open socket connection to remote host on port 80 */
  $fp = fsockopen($host, 8080, $errno, $errmsg, 30);
                                         
  /* check the connection */
  if (!$fp) {
    print "Cannot connect to $host!\n";
    return false;
  }
                                                         
  /* send request */
  fwrite($fp, $reqhead);
                                                            
  /* read response */
  $res = "";
  while(!feof($fp)) {
    $res .= fgets($fp, 4096);
  }   
  fclose($fp);
                                                                             
  /* separate header and body */
  $neck = strpos($res, "\r\n\r\n");
  $head = substr($res, 0, $neck);
  $body = substr($res, $neck+4);
                                                                                    
  /* check HTTP status */
  $lines = explode("\r\n", $head);
  preg_match('/HTTP\/(\\d\\.\\d)\\s*(\\d+)\\s*(.*)/', $lines[0], $m);
  $status = $m[2];
                                                                                            
  if ($status == 200) {
    file_put_contents($local, $body);
    return(true);
  } else {
    return(false);
  }
}

function string_begins_with($string, $search) {
  return (strncmp($string, $search, strlen($search)) == 0);
}

function endElementHandler ($parser,$name){
  //echo "End of " . $name . "\n";
}

function startElementHandler ($parser,$name){
  //echo "Start of ". $name . "\n";
  global $currentElement;
  $currentElement = $name;
  
}

function characterDataHandler ($parser, $data) {
  //echo "char data " . $data . "\n";
  global $currentElement;
  global $currentName;
  global $isBuilding;
  global $isBroken;
  if ($currentElement == "NAME") {
    $currentName = $data;
    //echo "Setting current name to " . $currentName . "\n";
  } else if ($currentElement == COLOR) {
     //ignore all non-Java and non-mir www. projects for our bears
     //not really needed seeing as we have our own hudson instance but worth keeping for future reference
    $ignore = (string_begins_with($currentName, "www.") or string_begins_with($currentName, "i18n.") OR string_begins_with($currentName, "ws."));
  
    if (!$ignore) {
      if(strpos($data, "anime") ? true : false) {
        echo $currentName . " is building\n";
        $isBuilding = true;
      }

      if(string_begins_with($data, "red")) {
        echo $currentName . " is broken\n";
        $isBroken = true;
      }
   } else {
      echo "Ignoring " . $currentName . "\n";
   }
  }
}

function switchSocket($socket, $command) {
  // Use syntax of your specific socket manufacturer here.
  file(NETCONTROL_URL . "?F$socket=$command");
}

global $isBuilding;
global $isBroken;

// Enter infinite loop.
while (true) {
  $isBuilding = false;
  $isBroken = false;
  $currentDate = getdate();
  $sleepPeriod = 10;
  //1=mon, 2=tue etc
  // if mon - fri between 9am and 9pm on else off and sleep
  if ($currentDate[wday] >= 1 && $currentDate[wday] <=5 && $currentDate[hours] >=9 && $currentDate[hours] <=21) {
    // Retrieve XML data from Hudson.
    $myxml = file(HUDSON_XML_URL);
    $hudsonXMLFile = "/tmp/hudson.xml";
    $res = get_file(HUDSON_XML_URL, $hudsonXMLFile);

    if (!$res) {
      die ("Could not retrieve xml from " . HUDSON_XML_URL);
    }
    if (!($fp=@fopen($hudsonXMLFile, "r"))) {
      die ("Couldn't open XML");
    }

    if (! ($xml_parser = xml_parser_create()) ) { 
      die ("Cannot create XML parser");
    }
    xml_set_element_handler($xml_parser,"startElementHandler","endElementHandler");
    xml_set_character_data_handler( $xml_parser, "characterDataHandler");
    while( $data = fread($fp, 4096)) {
      if(!xml_parse($xml_parser, $data, feof($fp))) {
        break;
      }
    }
    xml_parser_free($xml_parser);

    // Set yellow "Is Building" status light.
    if($isBuilding) {
      switchSocket(YELLOW_BEAR, SWITCH_ON);
    } else {
      switchSocket(YELLOW_BEAR, SWITCH_OFF);
    }

    // Set green/red "Build Outcome" status lights.
    if($isBroken) {
      switchSocket(GREEN_BEAR, SWITCH_OFF);
      switchSocket(RED_BEAR, SWITCH_ON);
    } else {
      switchSocket(RED_BEAR, SWITCH_OFF);
      switchSocket(GREEN_BEAR, SWITCH_ON);
    }
    $sleepPeriod = 10;
  } else {
    echo "Outside work hours, turning bears off to save power\n";
    switchSocket(GREEN_BEAR, SWITCH_OFF);
    switchSocket(RED_BEAR, SWITCH_OFF);
    switchSocket(YELLOW_BEAR, SWITCH_OFF);
    $sleepPeriod = 300; //5 minutes
  }

  sleep($sleepPeriod);
  echo "\n";
} //while (true)

?>


