namespace ACE_RFID
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.lblUid = new System.Windows.Forms.Label();
            this.button1 = new System.Windows.Forms.Button();
            this.button2 = new System.Windows.Forms.Button();
            this.materialType = new System.Windows.Forms.ComboBox();
            this.colorDialog1 = new System.Windows.Forms.ColorDialog();
            this.button3 = new System.Windows.Forms.Button();
            this.materialWeight = new System.Windows.Forms.ComboBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.label1 = new System.Windows.Forms.Label();
            this.lblMsg = new System.Windows.Forms.Label();
            this.lblTemps = new System.Windows.Forms.Label();
            this.lblConnect = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // lblUid
            // 
            this.lblUid.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblUid.Location = new System.Drawing.Point(112, 352);
            this.lblUid.Name = "lblUid";
            this.lblUid.Size = new System.Drawing.Size(208, 25);
            this.lblUid.TabIndex = 0;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.RoyalBlue;
            this.button1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button1.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.button1.ForeColor = System.Drawing.Color.White;
            this.button1.Location = new System.Drawing.Point(28, 442);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(126, 47);
            this.button1.TabIndex = 1;
            this.button1.Text = "Read Tag";
            this.button1.UseVisualStyleBackColor = false;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // button2
            // 
            this.button2.BackColor = System.Drawing.Color.RoyalBlue;
            this.button2.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button2.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.button2.ForeColor = System.Drawing.Color.White;
            this.button2.Location = new System.Drawing.Point(219, 442);
            this.button2.Name = "button2";
            this.button2.Size = new System.Drawing.Size(126, 47);
            this.button2.TabIndex = 3;
            this.button2.Text = "Write Tag";
            this.button2.UseVisualStyleBackColor = false;
            this.button2.Click += new System.EventHandler(this.button2_Click);
            // 
            // materialType
            // 
            this.materialType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.materialType.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.materialType.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.materialType.FormattingEnabled = true;
            this.materialType.Items.AddRange(new object[] {
            "ABS",
            "ASA",
            "PETG",
            "PLA",
            "PLA+",
            "PLA Glow",
            "PLA High Speed",
            "PLA Marble",
            "PLA Matte",
            "PLA SE",
            "PLA Silk",
            "TPU"});
            this.materialType.Location = new System.Drawing.Point(61, 52);
            this.materialType.Name = "materialType";
            this.materialType.Size = new System.Drawing.Size(259, 40);
            this.materialType.TabIndex = 4;
            // 
            // colorDialog1
            // 
            this.colorDialog1.AnyColor = true;
            // 
            // button3
            // 
            this.button3.BackColor = System.Drawing.SystemColors.Control;
            this.button3.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button3.Location = new System.Drawing.Point(61, 174);
            this.button3.Name = "button3";
            this.button3.Size = new System.Drawing.Size(259, 40);
            this.button3.TabIndex = 5;
            this.button3.UseVisualStyleBackColor = false;
            this.button3.Click += new System.EventHandler(this.button3_Click);
            // 
            // materialWeight
            // 
            this.materialWeight.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.materialWeight.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.materialWeight.Font = new System.Drawing.Font("Microsoft Sans Serif", 14F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.materialWeight.FormattingEnabled = true;
            this.materialWeight.Items.AddRange(new object[] {
            "1 KG",
            "750 G",
            "600 G",
            "500 G",
            "250 G"});
            this.materialWeight.Location = new System.Drawing.Point(61, 113);
            this.materialWeight.Name = "materialWeight";
            this.materialWeight.Size = new System.Drawing.Size(259, 40);
            this.materialWeight.TabIndex = 9;
            // 
            // checkBox1
            // 
            this.checkBox1.AutoSize = true;
            this.checkBox1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.checkBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.checkBox1.Location = new System.Drawing.Point(87, 292);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.checkBox1.Size = new System.Drawing.Size(233, 24);
            this.checkBox1.TabIndex = 10;
            this.checkBox1.Text = "  ?Auto read on tag scan ";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(38, 352);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(68, 20);
            this.label1.TabIndex = 11;
            this.label1.Text = "Tag ID:";
            // 
            // lblMsg
            // 
            this.lblMsg.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblMsg.Location = new System.Drawing.Point(33, 393);
            this.lblMsg.Name = "lblMsg";
            this.lblMsg.Size = new System.Drawing.Size(312, 31);
            this.lblMsg.TabIndex = 12;
            this.lblMsg.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // lblTemps
            // 
            this.lblTemps.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblTemps.Location = new System.Drawing.Point(57, 229);
            this.lblTemps.Name = "lblTemps";
            this.lblTemps.Size = new System.Drawing.Size(263, 51);
            this.lblTemps.TabIndex = 13;
            // 
            // lblConnect
            // 
            this.lblConnect.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblConnect.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblConnect.ForeColor = System.Drawing.Color.IndianRed;
            this.lblConnect.Location = new System.Drawing.Point(13, 33);
            this.lblConnect.Name = "lblConnect";
            this.lblConnect.Size = new System.Drawing.Size(351, 456);
            this.lblConnect.TabIndex = 15;
            this.lblConnect.Text = "Failed to find a ACR122 reader\r\n\r\n\r\nClick to connect to ACR122";
            this.lblConnect.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblConnect.Click += new System.EventHandler(this.lblConnect_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(376, 517);
            this.Controls.Add(this.lblConnect);
            this.Controls.Add(this.lblTemps);
            this.Controls.Add(this.lblMsg);
            this.Controls.Add(this.button3);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.checkBox1);
            this.Controls.Add(this.materialWeight);
            this.Controls.Add(this.materialType);
            this.Controls.Add(this.button2);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.lblUid);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "Form1";
            this.Text = "Ace RFID";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblUid;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button button2;
        private System.Windows.Forms.ComboBox materialType;
        private System.Windows.Forms.ColorDialog colorDialog1;
        private System.Windows.Forms.Button button3;
        private System.Windows.Forms.ComboBox materialWeight;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label lblMsg;
        private System.Windows.Forms.Label lblTemps;
        private System.Windows.Forms.Label lblConnect;
    }
}

