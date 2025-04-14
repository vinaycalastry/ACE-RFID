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
            this.btnRead = new System.Windows.Forms.Button();
            this.btnWrite = new System.Windows.Forms.Button();
            this.materialType = new System.Windows.Forms.ComboBox();
            this.colorDialog1 = new System.Windows.Forms.ColorDialog();
            this.btnColor = new System.Windows.Forms.Button();
            this.materialWeight = new System.Windows.Forms.ComboBox();
            this.checkBox1 = new System.Windows.Forms.CheckBox();
            this.label1 = new System.Windows.Forms.Label();
            this.lblMsg = new System.Windows.Forms.Label();
            this.lblTemps = new System.Windows.Forms.Label();
            this.lblConnect = new System.Windows.Forms.Label();
            this.panel1 = new System.Windows.Forms.Panel();
            this.label2 = new System.Windows.Forms.Label();
            this.btnSave = new System.Windows.Forms.Button();
            this.checkBox2 = new System.Windows.Forms.CheckBox();
            this.cboType = new System.Windows.Forms.ComboBox();
            this.cboVendor = new System.Windows.Forms.ComboBox();
            this.txtSerial = new System.Windows.Forms.TextBox();
            this.btnAdd = new System.Windows.Forms.Button();
            this.btnEdit = new System.Windows.Forms.Button();
            this.btnDel = new System.Windows.Forms.Button();
            this.btnCls = new System.Windows.Forms.Button();
            this.txtExtMin = new System.Windows.Forms.TextBox();
            this.txtExtMax = new System.Windows.Forms.TextBox();
            this.txtBedMin = new System.Windows.Forms.TextBox();
            this.txtBedMax = new System.Windows.Forms.TextBox();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // lblUid
            // 
            this.lblUid.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblUid.Location = new System.Drawing.Point(118, 370);
            this.lblUid.Name = "lblUid";
            this.lblUid.Size = new System.Drawing.Size(208, 25);
            this.lblUid.TabIndex = 0;
            // 
            // btnRead
            // 
            this.btnRead.BackColor = System.Drawing.Color.RoyalBlue;
            this.btnRead.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnRead.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnRead.ForeColor = System.Drawing.Color.White;
            this.btnRead.Location = new System.Drawing.Point(34, 460);
            this.btnRead.Name = "btnRead";
            this.btnRead.Size = new System.Drawing.Size(126, 47);
            this.btnRead.TabIndex = 1;
            this.btnRead.Text = "Read Tag";
            this.btnRead.UseVisualStyleBackColor = false;
            this.btnRead.Click += new System.EventHandler(this.btnRead_Click);
            // 
            // btnWrite
            // 
            this.btnWrite.BackColor = System.Drawing.Color.RoyalBlue;
            this.btnWrite.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnWrite.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnWrite.ForeColor = System.Drawing.Color.White;
            this.btnWrite.Location = new System.Drawing.Point(225, 460);
            this.btnWrite.Name = "btnWrite";
            this.btnWrite.Size = new System.Drawing.Size(126, 47);
            this.btnWrite.TabIndex = 3;
            this.btnWrite.Text = "Write Tag";
            this.btnWrite.UseVisualStyleBackColor = false;
            this.btnWrite.Click += new System.EventHandler(this.btnWrite_Click);
            // 
            // materialType
            // 
            this.materialType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.materialType.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.materialType.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.materialType.FormattingEnabled = true;
            this.materialType.Location = new System.Drawing.Point(67, 70);
            this.materialType.Name = "materialType";
            this.materialType.Size = new System.Drawing.Size(259, 37);
            this.materialType.TabIndex = 4;
            this.materialType.SelectedIndexChanged += new System.EventHandler(this.materialType_SelectedIndexChanged);
            // 
            // colorDialog1
            // 
            this.colorDialog1.AnyColor = true;
            // 
            // btnColor
            // 
            this.btnColor.BackColor = System.Drawing.SystemColors.Control;
            this.btnColor.FlatAppearance.BorderSize = 0;
            this.btnColor.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnColor.Location = new System.Drawing.Point(67, 192);
            this.btnColor.Name = "btnColor";
            this.btnColor.Size = new System.Drawing.Size(259, 40);
            this.btnColor.TabIndex = 5;
            this.btnColor.UseVisualStyleBackColor = false;
            this.btnColor.Click += new System.EventHandler(this.btnColor_Click);
            // 
            // materialWeight
            // 
            this.materialWeight.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.materialWeight.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.materialWeight.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.materialWeight.FormattingEnabled = true;
            this.materialWeight.Items.AddRange(new object[] {
            "1 KG",
            "750 G",
            "600 G",
            "500 G",
            "250 G"});
            this.materialWeight.Location = new System.Drawing.Point(67, 131);
            this.materialWeight.Name = "materialWeight";
            this.materialWeight.Size = new System.Drawing.Size(259, 37);
            this.materialWeight.TabIndex = 9;
            // 
            // checkBox1
            // 
            this.checkBox1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.checkBox1.Location = new System.Drawing.Point(67, 301);
            this.checkBox1.Name = "checkBox1";
            this.checkBox1.RightToLeft = System.Windows.Forms.RightToLeft.Yes;
            this.checkBox1.Size = new System.Drawing.Size(259, 33);
            this.checkBox1.TabIndex = 10;
            this.checkBox1.Text = "  ?Auto read on tag scan ";
            this.checkBox1.UseVisualStyleBackColor = true;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label1.Location = new System.Drawing.Point(44, 370);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(68, 20);
            this.label1.TabIndex = 11;
            this.label1.Text = "Tag ID:";
            // 
            // lblMsg
            // 
            this.lblMsg.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblMsg.Location = new System.Drawing.Point(39, 411);
            this.lblMsg.Name = "lblMsg";
            this.lblMsg.Size = new System.Drawing.Size(312, 31);
            this.lblMsg.TabIndex = 12;
            this.lblMsg.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            // 
            // lblTemps
            // 
            this.lblTemps.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblTemps.Location = new System.Drawing.Point(63, 247);
            this.lblTemps.Name = "lblTemps";
            this.lblTemps.Size = new System.Drawing.Size(263, 51);
            this.lblTemps.TabIndex = 13;
            // 
            // lblConnect
            // 
            this.lblConnect.Cursor = System.Windows.Forms.Cursors.Hand;
            this.lblConnect.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblConnect.ForeColor = System.Drawing.Color.IndianRed;
            this.lblConnect.Location = new System.Drawing.Point(935, 2);
            this.lblConnect.Name = "lblConnect";
            this.lblConnect.Size = new System.Drawing.Size(387, 520);
            this.lblConnect.TabIndex = 15;
            this.lblConnect.Text = "Failed to find a ACR122 reader\r\n\r\n\r\nClick to connect to ACR122";
            this.lblConnect.TextAlign = System.Drawing.ContentAlignment.MiddleCenter;
            this.lblConnect.Click += new System.EventHandler(this.lblConnect_Click);
            // 
            // panel1
            // 
            this.panel1.BackColor = System.Drawing.SystemColors.Control;
            this.panel1.Controls.Add(this.label6);
            this.panel1.Controls.Add(this.label5);
            this.panel1.Controls.Add(this.label4);
            this.panel1.Controls.Add(this.label3);
            this.panel1.Controls.Add(this.txtBedMax);
            this.panel1.Controls.Add(this.txtBedMin);
            this.panel1.Controls.Add(this.txtExtMax);
            this.panel1.Controls.Add(this.txtExtMin);
            this.panel1.Controls.Add(this.txtSerial);
            this.panel1.Controls.Add(this.btnCls);
            this.panel1.Controls.Add(this.cboVendor);
            this.panel1.Controls.Add(this.label2);
            this.panel1.Controls.Add(this.btnSave);
            this.panel1.Controls.Add(this.cboType);
            this.panel1.Controls.Add(this.checkBox2);
            this.panel1.Location = new System.Drawing.Point(466, 2);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(387, 520);
            this.panel1.TabIndex = 16;
            this.panel1.Visible = false;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.BackColor = System.Drawing.SystemColors.Control;
            this.label2.Font = new System.Drawing.Font("Microsoft Sans Serif", 7F);
            this.label2.ForeColor = System.Drawing.Color.CornflowerBlue;
            this.label2.Location = new System.Drawing.Point(73, 253);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(194, 17);
            this.label2.TabIndex = 7;
            this.label2.Text = "e.g. Basic, Matte, Silk, Marble";
            // 
            // btnSave
            // 
            this.btnSave.BackColor = System.Drawing.Color.RoyalBlue;
            this.btnSave.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnSave.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnSave.ForeColor = System.Drawing.Color.White;
            this.btnSave.Location = new System.Drawing.Point(220, 456);
            this.btnSave.Name = "btnSave";
            this.btnSave.Size = new System.Drawing.Size(103, 48);
            this.btnSave.TabIndex = 6;
            this.btnSave.Text = "Save";
            this.btnSave.UseVisualStyleBackColor = false;
            this.btnSave.Click += new System.EventHandler(this.btnSave_Click);
            // 
            // checkBox2
            // 
            this.checkBox2.AutoSize = true;
            this.checkBox2.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.checkBox2.Location = new System.Drawing.Point(67, 111);
            this.checkBox2.Name = "checkBox2";
            this.checkBox2.Size = new System.Drawing.Size(225, 24);
            this.checkBox2.TabIndex = 5;
            this.checkBox2.Text = "Can\'t find vendor I want";
            this.checkBox2.UseVisualStyleBackColor = true;
            this.checkBox2.CheckedChanged += new System.EventHandler(this.checkBox2_CheckedChanged);
            // 
            // cboType
            // 
            this.cboType.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboType.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.cboType.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.cboType.FormattingEnabled = true;
            this.cboType.Location = new System.Drawing.Point(64, 157);
            this.cboType.Name = "cboType";
            this.cboType.Size = new System.Drawing.Size(259, 37);
            this.cboType.TabIndex = 4;
            this.cboType.SelectedIndexChanged += new System.EventHandler(this.cboType_SelectedIndexChanged);
            // 
            // cboVendor
            // 
            this.cboVendor.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.cboVendor.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.cboVendor.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.cboVendor.FormattingEnabled = true;
            this.cboVendor.Location = new System.Drawing.Point(64, 67);
            this.cboVendor.Name = "cboVendor";
            this.cboVendor.Size = new System.Drawing.Size(259, 37);
            this.cboVendor.TabIndex = 3;
            // 
            // txtSerial
            // 
            this.txtSerial.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.txtSerial.Font = new System.Drawing.Font("Microsoft Sans Serif", 12F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.txtSerial.Location = new System.Drawing.Point(64, 215);
            this.txtSerial.Name = "txtSerial";
            this.txtSerial.Size = new System.Drawing.Size(259, 35);
            this.txtSerial.TabIndex = 2;
            this.txtSerial.WordWrap = false;
            // 
            // btnAdd
            // 
            this.btnAdd.FlatAppearance.BorderSize = 0;
            this.btnAdd.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnAdd.Image = global::ACE_RFID.Properties.Resources.add;
            this.btnAdd.Location = new System.Drawing.Point(278, 2);
            this.btnAdd.Name = "btnAdd";
            this.btnAdd.Size = new System.Drawing.Size(48, 48);
            this.btnAdd.TabIndex = 18;
            this.btnAdd.UseVisualStyleBackColor = true;
            this.btnAdd.Click += new System.EventHandler(this.btnAdd_Click);
            // 
            // btnEdit
            // 
            this.btnEdit.FlatAppearance.BorderSize = 0;
            this.btnEdit.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnEdit.Image = global::ACE_RFID.Properties.Resources.edit;
            this.btnEdit.Location = new System.Drawing.Point(213, 2);
            this.btnEdit.Name = "btnEdit";
            this.btnEdit.Size = new System.Drawing.Size(48, 48);
            this.btnEdit.TabIndex = 19;
            this.btnEdit.UseVisualStyleBackColor = true;
            this.btnEdit.Click += new System.EventHandler(this.btnEdit_Click);
            // 
            // btnDel
            // 
            this.btnDel.FlatAppearance.BorderSize = 0;
            this.btnDel.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnDel.Image = global::ACE_RFID.Properties.Resources.delete;
            this.btnDel.Location = new System.Drawing.Point(142, 2);
            this.btnDel.Name = "btnDel";
            this.btnDel.Size = new System.Drawing.Size(48, 48);
            this.btnDel.TabIndex = 20;
            this.btnDel.UseVisualStyleBackColor = true;
            this.btnDel.Click += new System.EventHandler(this.btnDel_Click);
            // 
            // btnCls
            // 
            this.btnCls.BackColor = System.Drawing.Color.RoyalBlue;
            this.btnCls.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.btnCls.Font = new System.Drawing.Font("Microsoft Sans Serif", 10F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.btnCls.ForeColor = System.Drawing.Color.White;
            this.btnCls.Location = new System.Drawing.Point(64, 456);
            this.btnCls.Name = "btnCls";
            this.btnCls.Size = new System.Drawing.Size(104, 49);
            this.btnCls.TabIndex = 8;
            this.btnCls.Text = "Cancel";
            this.btnCls.UseVisualStyleBackColor = false;
            this.btnCls.Click += new System.EventHandler(this.btnCls_Click);
            // 
            // txtExtMin
            // 
            this.txtExtMin.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.txtExtMin.Location = new System.Drawing.Point(206, 289);
            this.txtExtMin.MaxLength = 3;
            this.txtExtMin.Name = "txtExtMin";
            this.txtExtMin.Size = new System.Drawing.Size(117, 26);
            this.txtExtMin.TabIndex = 9;
            this.txtExtMin.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtExtMin.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtExtMin_KeyPress);
            // 
            // txtExtMax
            // 
            this.txtExtMax.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.txtExtMax.Location = new System.Drawing.Point(206, 325);
            this.txtExtMax.MaxLength = 3;
            this.txtExtMax.Name = "txtExtMax";
            this.txtExtMax.Size = new System.Drawing.Size(117, 26);
            this.txtExtMax.TabIndex = 10;
            this.txtExtMax.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtExtMax.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtExtMax_KeyPress);
            // 
            // txtBedMin
            // 
            this.txtBedMin.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.txtBedMin.Location = new System.Drawing.Point(206, 360);
            this.txtBedMin.MaxLength = 3;
            this.txtBedMin.Name = "txtBedMin";
            this.txtBedMin.Size = new System.Drawing.Size(117, 26);
            this.txtBedMin.TabIndex = 11;
            this.txtBedMin.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtBedMin.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtBedMin_KeyPress);
            // 
            // txtBedMax
            // 
            this.txtBedMax.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
            this.txtBedMax.Location = new System.Drawing.Point(206, 395);
            this.txtBedMax.MaxLength = 3;
            this.txtBedMax.Name = "txtBedMax";
            this.txtBedMax.Size = new System.Drawing.Size(117, 26);
            this.txtBedMax.TabIndex = 12;
            this.txtBedMax.TextAlign = System.Windows.Forms.HorizontalAlignment.Center;
            this.txtBedMax.KeyPress += new System.Windows.Forms.KeyPressEventHandler(this.txtBedMax_KeyPress);
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label3.Location = new System.Drawing.Point(60, 291);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(115, 20);
            this.label3.TabIndex = 13;
            this.label3.Text = "Extruder Min:";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label4.Location = new System.Drawing.Point(60, 327);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(119, 20);
            this.label4.TabIndex = 14;
            this.label4.Text = "Extruder Max:";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label5.Location = new System.Drawing.Point(63, 362);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(79, 20);
            this.label5.TabIndex = 15;
            this.label5.Text = "Bed Min:";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.label6.Location = new System.Drawing.Point(63, 397);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(83, 20);
            this.label6.TabIndex = 16;
            this.label6.Text = "Bed Max:";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(9F, 20F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(384, 530);
            this.Controls.Add(this.lblConnect);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.btnWrite);
            this.Controls.Add(this.btnRead);
            this.Controls.Add(this.btnDel);
            this.Controls.Add(this.btnEdit);
            this.Controls.Add(this.btnAdd);
            this.Controls.Add(this.lblTemps);
            this.Controls.Add(this.lblMsg);
            this.Controls.Add(this.btnColor);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.checkBox1);
            this.Controls.Add(this.materialWeight);
            this.Controls.Add(this.materialType);
            this.Controls.Add(this.lblUid);
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MaximizeBox = false;
            this.Name = "Form1";
            this.Text = "Ace RFID";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.Load += new System.EventHandler(this.Form1_Load);
            this.panel1.ResumeLayout(false);
            this.panel1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label lblUid;
        private System.Windows.Forms.Button btnRead;
        private System.Windows.Forms.Button btnWrite;
        private System.Windows.Forms.ComboBox materialType;
        private System.Windows.Forms.ColorDialog colorDialog1;
        private System.Windows.Forms.Button btnColor;
        private System.Windows.Forms.ComboBox materialWeight;
        private System.Windows.Forms.CheckBox checkBox1;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label lblMsg;
        private System.Windows.Forms.Label lblTemps;
        private System.Windows.Forms.Label lblConnect;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.ComboBox cboVendor;
        private System.Windows.Forms.TextBox txtSerial;
        private System.Windows.Forms.ComboBox cboType;
        private System.Windows.Forms.CheckBox checkBox2;
        private System.Windows.Forms.Button btnAdd;
        private System.Windows.Forms.Button btnEdit;
        private System.Windows.Forms.Button btnDel;
        private System.Windows.Forms.Button btnSave;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Button btnCls;
        private System.Windows.Forms.TextBox txtBedMax;
        private System.Windows.Forms.TextBox txtBedMin;
        private System.Windows.Forms.TextBox txtExtMax;
        private System.Windows.Forms.TextBox txtExtMin;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label3;
    }
}

