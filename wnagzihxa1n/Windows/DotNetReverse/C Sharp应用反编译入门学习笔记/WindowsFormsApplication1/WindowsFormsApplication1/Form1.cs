using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            MessageBox.Show("Hello world");
        }

        private void button2_Click(object sender, EventArgs e)
        {
            listView1.Items.Add(new ListViewItem(listView1.Items.Count.ToString()));
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            listView1.Columns.Add("Test", 200, HorizontalAlignment.Center);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            if (listView1.Items.Count > 0)
            {
                listView1.Items[listView1.Items.Count - 1].Remove();
            }
        }

        private void settingToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Form2 form2 = new Form2();
            form2.ShowDialog();
        }

        private void exitToolStripMenuItem_Click(object sender, EventArgs e)
        {
            System.Environment.Exit(0);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            this.Text = "emmmmmmmmmmmmmmmmmmmmm";
        }
    }
}
