#include <SPI.h>
#include <WiFi.h>
#include <LEAmDNS.h>
#include <WebServer.h>
#include <LittleFS.h>
#include "src/includes.h"

#define SS_PIN 5
#define RST_PIN 22
#define MISO_PIN 4
#define MOSI_PIN 7
#define SCK_PIN 6
#define SPK_PIN 19

MFRC522 mfrc522(SS_PIN, RST_PIN);
WebServer webServer(80);

String filamentType = "PLA";
String filamentColor = "0000FF";
String filamentLen = "330";
IPAddress Server_IP(10, 1, 0, 1);
IPAddress Subnet_Mask(255, 255, 255, 0);
String AP_SSID = "ACE_RFID";
String AP_PASS = "password";
String WIFI_SSID = "";
String WIFI_PASS = "";
String WIFI_HOSTNAME = "ace.local";

void setup()
{
  SPI.setMISO(MISO_PIN);
  SPI.setCS(SS_PIN);
  SPI.setSCK(SCK_PIN);
  SPI.setMOSI(MOSI_PIN);
  SPI.begin();
  mfrc522.PCD_Init();
  pinMode(SPK_PIN, OUTPUT);

  webServer.on("/config", HTTP_GET, handleConfig);
  webServer.on("/index.html", HTTP_GET, handleIndex);
  webServer.on("/", HTTP_GET, handleIndex);
  webServer.on("/config", HTTP_POST, handleConfigP);
  webServer.on("/spooldata", HTTP_POST, handleSpoolData);
  webServer.onNotFound(handle404);
  webServer.begin();
}

void setup1()
{
  LittleFS.begin();
  loadConfig();
  if (WIFI_SSID != "" && WIFI_PASS != "")
  {
    WiFi.hostname(WIFI_HOSTNAME.c_str());
    WiFi.begin(WIFI_SSID.c_str(), WIFI_PASS.c_str());
    if (WiFi.waitForConnectResult() == WL_CONNECTED)
    {
      IPAddress LAN_IP = WiFi.localIP();
    }else
    {
      fallbackAP();
    }
  }else
  {
    fallbackAP();
  }

  if (WIFI_HOSTNAME != "")
  {
    String mdnsHost = WIFI_HOSTNAME;
    mdnsHost.replace(".local", "");
    MDNS.begin(mdnsHost.c_str());
  }
}

void fallbackAP()
{
  if (AP_SSID == "" || AP_PASS == "")
  {
    AP_SSID = "ACE_RFID";
    AP_PASS = "password";
  }
  WiFi.softAPConfig(Server_IP, Server_IP, Subnet_Mask);
  WiFi.softAP(AP_SSID.c_str(), AP_PASS.c_str());
  WiFi.softAPConfig(Server_IP, Server_IP, Subnet_Mask);
}

void loop1()
{
  MDNS.update();
}


void loop()
{
  webServer.handleClient();
  if (!mfrc522.PICC_IsNewCardPresent())
    return;

  if (!mfrc522.PICC_ReadCardSerial())
    return;


  MFRC522::PICC_Type piccType = mfrc522.PICC_GetType(mfrc522.uid.sak);
  if (piccType != MFRC522::PICC_TYPE_MIFARE_UL)
  {
    tone(SPK_PIN, 400, 400);
    delay(2000);
    return;
  }


  byte hdrData[4] = {0x7B, 0x00, 0x65, 0x00};
  mfrc522.MIFARE_Ultralight_Write(4, hdrData, 4);


  byte sku[20];
  GetSku(filamentType, sku);
  for (int i = 0; i < 6; i++)
  {
    mfrc522.MIFARE_Ultralight_Write(5 + i, &sku[i * 4], 4);
  }


  byte brand[] = {
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00
  };


  for (int i = 0; i < 6; i++)
  {
    mfrc522.MIFARE_Ultralight_Write(10 + i, &brand[i * 4], 4);
  }


  byte matData[16] = {
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00,
    0x00, 0x00, 0x00, 0x00
  };


  for (int i = 0; i < filamentType.length(); i++) {
    matData[i] = (byte)filamentType.charAt(i);
  }


  for (int i = 0; i < 4; i++)
  {
    mfrc522.MIFARE_Ultralight_Write(15 + i, &matData[ i * 4 ], 4);
  }


  byte color[4];
  hexToByte(filamentColor.c_str(), color);
  revArray(color);
  mfrc522.MIFARE_Ultralight_Write(20, color, 4);


  int temps[4];
  GetTemps(filamentType, temps);

  byte extTemp[4];
  byte extMin[2];
  byte extMax[2];
  intToByte(temps[0], extMin);
  intToByte(temps[1], extMax);
  combineArray(extMin, extMax, extTemp);
  mfrc522.MIFARE_Ultralight_Write(24, extTemp, 4);


  byte bedTemp[4];
  byte bedMin[2];
  byte bedMax[2];
  intToByte(temps[2], bedMin);
  intToByte(temps[3], bedMax);
  combineArray(bedMin, bedMax, bedTemp);
  mfrc522.MIFARE_Ultralight_Write(29, bedTemp, 4);


  byte filData[4];
  byte diameter[2];
  byte length[2];
  intToByte(175, diameter);
  intToByte(filamentLen.toInt(), length);
  combineArray(diameter, length, filData);
  mfrc522.MIFARE_Ultralight_Write(30, filData, 4);


  byte unkData[4] = {0xE8, 0x03, 0x00, 0x00};
  mfrc522.MIFARE_Ultralight_Write(31, unkData, 4);

  mfrc522.PICC_HaltA();
  tone(SPK_PIN, 1000, 200);
  delay(2000);
}


void handleIndex()
{
  webServer.send_P(200, "text/html", indexData);
}

void handle404()
{
  webServer.send(404, "text/plain", "Not Found");
}

void handleConfig()
{
  String htmStr = AP_SSID + "|-|" + WIFI_SSID + "|-|" + WIFI_HOSTNAME;
  webServer.setContentLength(htmStr.length());
  webServer.send(200, "text/plain", htmStr);
}

void handleConfigP()
{
  if (webServer.hasArg("ap_ssid") && webServer.hasArg("ap_pass") && webServer.hasArg("wifi_ssid") && webServer.hasArg("wifi_pass") && webServer.hasArg("wifi_host"))
  {
    AP_SSID = webServer.arg("ap_ssid");
    if (!webServer.arg("ap_pass").equals("********"))
    {
      AP_PASS = webServer.arg("ap_pass");
    }
    WIFI_SSID = webServer.arg("wifi_ssid");
    if (!webServer.arg("wifi_pass").equals("********"))
    {
      WIFI_PASS = webServer.arg("wifi_pass");
    }
    WIFI_HOSTNAME = webServer.arg("wifi_host");

    File file = LittleFS.open("/config.ini", "w");
    if (file)
    {
      file.print("\r\nAP_SSID=" + AP_SSID + "\r\nAP_PASS=" + AP_PASS + "\r\nWIFI_SSID=" + WIFI_SSID + "\r\nWIFI_PASS=" + WIFI_PASS + "\r\nWIFI_HOST=" + WIFI_HOSTNAME + "\r\n");
      file.close();
    }
    String htmStr = "OK";
    webServer.setContentLength(htmStr.length());
    webServer.send(200, "text/plain", htmStr);
    delay(1000);
    rp2040.restart();
  }
  else
  {
    webServer.send(417, "text/plain", "Expectation Failed");
  }
}

void handleSpoolData()
{
  if (webServer.hasArg("materialColor") && webServer.hasArg("materialType") && webServer.hasArg("materialWeight"))
  {
    filamentColor = webServer.arg("materialColor");
    filamentColor.replace("#", "");
    filamentType = webServer.arg("materialType");
    filamentLen = String(GetMaterialLength(webServer.arg("materialWeight")));
    File file = LittleFS.open("/spool.ini", "w");
    if (file)
    {
      file.print("MAT=" + filamentType + "\r\nCOL=" + filamentColor + "\r\nLEN=" + filamentLen);
      file.close();
    }
    String htmStr = "OK";
    webServer.setContentLength(htmStr.length());
    webServer.send(200, "text/plain", htmStr);
  }
  else
  {
    webServer.send(417, "text/plain", "Expectation Failed");
  }
}

int GetMaterialLength(String materialWeight)
{
  if (materialWeight == "1 KG")
  {
    return 330;
  }
  else if (materialWeight == "750 G")
  {
    return 247;
  }
  else if (materialWeight == "600 G")
  {
    return 198;
  }
  else if (materialWeight == "500 G")
  {
    return 165;
  }
  else if (materialWeight == "250 G")
  {
    return 82;
  }
  return 330;
}

void GetTemps(String materialName, int temps[]) {
  if (materialName == "ABS")
  {
    temps[0] = 220;
    temps[1] = 250;
    temps[2] = 90;
    temps[3] = 100;
  }
  else if (materialName == "ASA")
  {
    temps[0] = 240;
    temps[1] = 280;
    temps[2] = 90;
    temps[3] = 100;
  }
  else if (materialName == "PLA" || materialName == "PLA High Speed" || materialName == "PLA Glow")
  {
    temps[0] = 190;
    temps[1] = 230;
    temps[2] = 50;
    temps[3] = 60;
  }
  else if (materialName == "PLA Marble")
  {
    temps[0] = 200;
    temps[1] = 230;
    temps[2] = 50;
    temps[3] = 60;
  }
  else if (materialName == "PLA Matte" || materialName == "PLA SE" )
  {
    temps[0] = 190;
    temps[1] = 230;
    temps[2] = 55;
    temps[3] = 65;
  }
  else if (materialName == "PLA Silk")
  {
    temps[0] = 200;
    temps[1] = 230;
    temps[2] = 55;
    temps[3] = 65;
  }
  else if (materialName == "PETG")
  {
    temps[0] = 230;
    temps[1] = 250;
    temps[2] = 70;
    temps[3] = 90;
  }
  else if (materialName == "TPU")
  {
    temps[0] = 210;
    temps[1] = 230;
    temps[2] = 25;
    temps[3] = 60;
  }
  else if (materialName == "PLA+")
  {
    temps[0] = 210;
    temps[1] = 230;
    temps[2] = 45;
    temps[3] = 60;
  }
  else
  {
    temps[0] = 200;
    temps[1] = 210;
    temps[2] = 50;
    temps[3] = 60;
  }
}

void GetSku(String materialName, byte sku[]) {
  byte def [20] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  byte pla [20] = {65, 72, 80, 76, 76, 66, 45, 49, 48, 51, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  byte plap [20] = {65, 72, 80, 76, 80, 66, 82, 45, 49, 48, 50, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  if (materialName == "PLA")
  {
    for (int i = 0; i < 20; i++) {
      sku[i] = pla[i];
    }
  }
  else if (materialName == "PLA+")
  {
    for (int i = 0; i < 20; i++) {
      sku[i] = plap[i];
    }
  }
  else
  {
    for (int i = 0; i < 20; i++) {
      sku[i] = def[i];
    }
  }
}

String errorMsg(int errnum)
{
  if (errnum == UPDATE_ERROR_OK) {
    return "No Error";
  } else if (errnum == UPDATE_ERROR_WRITE) {
    return "Flash Write Failed";
  } else if (errnum == UPDATE_ERROR_ERASE) {
    return "Flash Erase Failed";
  } else if (errnum == UPDATE_ERROR_READ) {
    return "Flash Read Failed";
  } else if (errnum == UPDATE_ERROR_SPACE) {
    return "Not Enough Space";
  } else if (errnum == UPDATE_ERROR_SIZE) {
    return "Bad Size Given";
  } else if (errnum == UPDATE_ERROR_STREAM) {
    return "Stream Read Timeout";
  } else if (errnum == UPDATE_ERROR_MD5) {
    return "MD5 Check Failed";
  } else if (errnum == UPDATE_ERROR_MAGIC_BYTE) {
    return "Magic byte is wrong, not 0xE9";
  } else {
    return "UNKNOWN";
  }
}

void loadConfig()
{
  if (LittleFS.exists("/config.ini"))
  {
    File file = LittleFS.open("/config.ini", "r");
    if (file)
    {
      String iniData;
      while (file.available())
      {
        char chnk = file.read();
        iniData += chnk;
      }
      file.close();
      if (instr(iniData, "AP_SSID="))
      {
        AP_SSID = split(iniData, "AP_SSID=", "\r\n");
        AP_SSID.trim();
      }

      if (instr(iniData, "AP_PASS="))
      {
        AP_PASS = split(iniData, "AP_PASS=", "\r\n");
        AP_PASS.trim();
      }

      if (instr(iniData, "WIFI_SSID="))
      {
        WIFI_SSID = split(iniData, "WIFI_SSID=", "\r\n");
        WIFI_SSID.trim();
      }

      if (instr(iniData, "WIFI_PASS="))
      {
        WIFI_PASS = split(iniData, "WIFI_PASS=", "\r\n");
        WIFI_PASS.trim();
      }

      if (instr(iniData, "WIFI_HOST="))
      {
        WIFI_HOSTNAME = split(iniData, "WIFI_HOST=", "\r\n");
        WIFI_HOSTNAME.trim();
      }
    }
  }
  else
  {
    File file = LittleFS.open("/config.ini", "w");
    if (file)
    {
      file.print("\r\nAP_SSID=" + AP_SSID + "\r\nAP_PASS=" + AP_PASS + "\r\nWIFI_SSID=" + WIFI_SSID + "\r\nWIFI_PASS=" + WIFI_PASS + "\r\nWIFI_HOST=" + WIFI_HOSTNAME + "\r\n");
      file.close();
    }
  }

  if (LittleFS.exists("/spool.ini"))
  {
    File file = LittleFS.open("/spool.ini", "r");
    if (file)
    {
      String iniData;
      while (file.available())
      {
        char chnk = file.read();
        iniData += chnk;
      }
      file.close();
      if (instr(iniData, "MAT="))
      {
        filamentType = split(iniData, "MAT=", "\r\n");
        filamentType.trim();
      }
      else
      {
        filamentType = "PLA";
      }
      if (instr(iniData, "COL="))
      {
        filamentColor = split(iniData, "COL=", "\r\n");
        filamentColor.trim();
      }
      else
      {
        filamentColor = "0000FF";
      }
      if (instr(iniData, "LEN="))
      {
        filamentLen = split(iniData, "LEN=", "\r\n");
        filamentLen.trim();
      }
      else
      {
        filamentLen = "330";
      }
    }
  }
  else
  {
    File file = LittleFS.open("/spool.ini", "w");
    if (file)
    {
      file.print("MAT=PLA\r\nCOL=0000FF\r\nLEN=330");
      file.close();
    }
  }
}

String split(String str, String from, String to)
{
  String tmpstr = str;
  tmpstr.toLowerCase();
  from.toLowerCase();
  to.toLowerCase();
  int pos1 = tmpstr.indexOf(from);
  int pos2 = tmpstr.indexOf(to, pos1 + from.length());
  String retval = str.substring(pos1 + from.length(), pos2);
  return retval;
}

bool instr(String str, String search)
{
  int result = str.indexOf(search);
  if (result == -1)
  {
    return false;
  }
  return true;
}

void intToByte(int value, byte* byteArray) {
  byteArray[0] = value & 0xFF;
  byteArray[1] = (value >> 8) & 0xFF;
}

void hexToByte(const char* hexString, byte* byteArray) {
  int arraySize = strlen(hexString) / 2;
  int stringLength = strlen(hexString);
  for (int i = 0; i < stringLength; i += 2) {
    char hexPair[3];
    hexPair[0] = hexString[i];
    hexPair[1] = hexString[i + 1];
    hexPair[2] = '\0';
    char* endPtr;
    byteArray[i / 2] = strtol(hexPair, &endPtr, 16);
  }
}

byte* revArray(byte* array) {
  int left = 0;
  int right = sizeof(array) - 1;
  while (left < right) {
    byte temp = array[left];
    array[left] = array[right];
    array[right] = temp;
    left++;
    right--;
  }
  return array;
}

void combineArray(byte* array1, byte* array2, byte* byteArray) {
  for (int i = 0; i < 2; i++) {
    byteArray[i] = array1[i];
  }
  for (int i = 0; i < 2; i++) {
    byteArray[2 + i] = array2[i];
  }
}
