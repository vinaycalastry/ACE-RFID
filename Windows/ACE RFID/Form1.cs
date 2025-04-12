using NfcDevice;
using System;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Threading;
using System.Media;

namespace ACE_RFID
{
    public partial class Form1 : Form
    {

        private ACR122U acr122u = new ACR122U();
        private PCSC.ICardReader reader;
        private String materialColor;


        public Form1()
        {
            InitializeComponent();
            acr122u.CardInserted += Acr122u_CardInserted;
            acr122u.CardRemoved += Acr122u_CardRemoved;
        }


        private void Form1_Load(object sender, EventArgs e)
        {

            BackColor = ColorTranslator.FromHtml("#F4F4F4");
            lblConnect.BackColor = ColorTranslator.FromHtml("#F4F4F4");
            button1.BackColor = ColorTranslator.FromHtml("#1976D2");
            button2.BackColor = ColorTranslator.FromHtml("#1976D2");

            materialType.Text = "PLA";
            materialWeight.Text = "1 KG";
            materialColor = "0000FF";
            button3.BackColor = ColorTranslator.FromHtml("#" + materialColor);

            try
            {
                lblConnect.Visible = false;
                acr122u.Init(true, 144, 4, 4, 200);
            }
            catch (Exception)
            {
                SystemSounds.Beep.Play();
                lblConnect.Visible = true;
            }
        }


        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            Environment.Exit(0);
        }


        private void Acr122u_CardInserted(PCSC.ICardReader r)
        {
            try
            {
                reader = r;
                Invoke((MethodInvoker)delegate ()
                {
                   lblUid.Text = BitConverter.ToString(acr122u.GetUID(reader)).Replace("-", " ");
                    if (checkBox1.Checked)
                    {
                        ReadTag();
                    }
                });
            }
            catch (Exception e)
            {
                ShowMsg(e.Message);
            }
        }


        private void Acr122u_CardRemoved()
        {
            try
            {
                Invoke((MethodInvoker)delegate ()
                {
                    lblUid.Text = string.Empty;
                });
            }
            catch (Exception e)
            {
                ShowMsg(e.Message);
            }
        }


        void WriteTag()
        {
            try
            {
                if (reader != null && reader.IsConnected)
                {

                    byte[] buffer = new byte[144];

                    new byte[] {123, 0, 101, 0}.CopyTo(buffer, 0);

                    Utils.GetSku(materialType.Text).CopyTo(buffer, 4); //sku

                    Utils.GetBrand(materialType.Text).CopyTo(buffer, 24); //brand

                    Encoding.UTF8.GetBytes(materialType.Text).CopyTo(buffer, 44); //type

                    buffer[64] = (byte)0xFF;
                    Utils.ParseColor(materialColor).CopyTo(buffer, 65); //color

                    Utils.NumToBytes(Utils.GetTemps(materialType.Text)[0]).CopyTo(buffer, 80); //ext min
                    Utils.NumToBytes(Utils.GetTemps(materialType.Text)[1]).CopyTo(buffer, 82); //ext max
                    Utils.NumToBytes(Utils.GetTemps(materialType.Text)[2]).CopyTo(buffer, 100); //bed min
                    Utils.NumToBytes(Utils.GetTemps(materialType.Text)[3]).CopyTo(buffer, 102); //bed max

                    Utils.NumToBytes(175).CopyTo(buffer, 104); //diameter
                    Utils.NumToBytes(Utils.GetMaterialLength(materialWeight.Text)).CopyTo(buffer, 106); //length


                    acr122u.WriteData(reader, buffer);

                    ShowMsg("Data written to TAG");
                }
            }
            catch (Exception)
            {
                ShowMsg("Error writing to TAG");
            }
        }


        private void ReadTag()
        {
            try
            {
                if (reader != null && reader.IsConnected)
                {
                    byte[] buffer = acr122u.ReadData(reader);

                    // String sku = Encoding.UTF8.GetString(Utils.SubArray(buffer, 4, 20)).Trim();
                    // String Brand = Encoding.UTF8.GetString(Utils.SubArray(buffer, 24, 20)).Trim();

                    materialType.Text = Encoding.UTF8.GetString(Utils.SubArray(buffer, 44, 20)).Trim();

                    materialColor = Utils.ParseColor(Utils.SubArray(buffer, 65, 3));

                    button3.BackColor = ColorTranslator.FromHtml("#" + materialColor);

                    int extMin = Utils.ParseNumber(Utils.SubArray(buffer, 80, 2));
                    int extMax = Utils.ParseNumber(Utils.SubArray(buffer, 82, 2));
                    int bedMin = Utils.ParseNumber(Utils.SubArray(buffer, 100, 2));
                    int bedMax = Utils.ParseNumber(Utils.SubArray(buffer, 102, 2));

                    lblTemps.Text = String.Format("Extruder Temp: {0:d} min | {1:d} max\nBed Temp: {2:d} min | {3:d} max", extMin, extMax, bedMin, bedMax);


                    // int diameter = Utils.ParseNumber(Utils.SubArray(buffer,104,2));
                    materialWeight.Text = Utils.GetMaterialWeight(Utils.ParseNumber(Utils.SubArray(buffer, 106, 2)));

                    ShowMsg("Data read from TAG");
                }
            }
            catch (Exception)
            {
                ShowMsg("Error reading TAG");
            }
            
        }


        private void ShowMsg(String message)
        {
            Invoke((MethodInvoker)delegate ()
           {
               lblMsg.Text = message;
           });
            System.Threading.Timer timer = new System.Threading.Timer(t =>
            {
                Invoke((MethodInvoker)delegate ()
                {
                    lblMsg.Text = String.Empty;
                });
            }, null, 3000, Timeout.Infinite);
        }


        private void button1_Click(object sender, EventArgs e)
        {
            ReadTag();
        }


        private void button2_Click(object sender, EventArgs e)
        {
            WriteTag();
        }


        private void button3_Click(object sender, EventArgs e)
        {
            if (colorDialog1.ShowDialog() == DialogResult.OK)
            {
                button3.BackColor = colorDialog1.Color;
                materialColor = (colorDialog1.Color.ToArgb() & 0x00FFFFFF).ToString("X6");
            }
        }


        private void lblConnect_Click(object sender, EventArgs e)
        {
            try
            {
                lblConnect.Visible = false;
                acr122u.Init(true, 144, 4, 4, 200);
            }
            catch (Exception)
            {
                SystemSounds.Beep.Play();
                lblConnect.Visible = true;
            }
        }
    }
}
