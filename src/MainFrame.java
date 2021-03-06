import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The main Frame class of the application
 * 
 * @author M.Anwer, A.Aly, A.Abo ElHamaid
 */
public class MainFrame extends JFrame
{
	private static final long serialVersionUID = 1L;

	private StringBuilder path;
	private String output;
	private String simplifiedOutput;
	private Circuit circuit;
	private JButton item1;
	private JButton item2;
	private JButton item3;
	private JButton item4;
	private JButton item8;
	private JButton item9;
	private JButton item10;
	private JButton item11;
	private JPanel item7;

	// To check if there's an error in the circuit
	public static boolean failed = false;

	private Dimension ss = Toolkit.getDefaultToolkit().getScreenSize();

	public MainFrame()
	{
		super("States Creator");
		setLayout(new GridBagLayout());

		Dimension frameSize = new Dimension(300, 100);
		setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2
				- frameSize.height / 2, frameSize.width, frameSize.height);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(0, 5, 0, 5);
		c.ipadx = 20;
		c.ipady = 20;

		item1 = new JButton("Import input file");
		add(item1, c);

		item2 = new JButton("About Authors");
		add(item2, c);

		Handler hnd1 = new Handler();
		item1.addActionListener(hnd1);
		item2.addActionListener(hnd1);
	}

	private class SeconderyFrame extends JFrame
	{
		private static final long serialVersionUID = 1L;

		public SeconderyFrame(StringBuilder str, boolean are_equal)
		{
			super(str.toString());
			int h;
			if (are_equal)
				h = 160;
			else
				h = 260;
			Dimension frameSize = new Dimension(450, h);
			setBounds(ss.width / 2 - frameSize.width / 2, ss.height / 2
					- frameSize.height / 2, frameSize.width, frameSize.height);

			item3 = new JButton("Output");
			item4 = new JButton("Simplified Output");
			item8 = new JButton("Generate Output File");
			item9 = new JButton("Generate Simplified Output File");
			item10 = new JButton("Generate Output State Diagram");
			item11 = new JButton("Generate Simplified Output State Diagram");
			item7 = new JPanel(new GridBagLayout());

			GridBagConstraints c = new GridBagConstraints();
			c.gridx = 0;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.ipadx = 10;
			c.ipady = 10;

			c.gridy = 2;
			item7.add(item3, c);

			if (!are_equal)
			{

				c.gridy = 3;
				item7.add(item4, c);

				c.gridy = 4;
				item7.add(item8, c);

				c.gridy = 5;
				item7.add(item10, c);

				c.gridy = 6;
				item7.add(item9, c);

				c.gridy = 7;
				item7.add(item11, c);
			} else
			{
				c.gridy = 3;
				item7.add(item8, c);

				c.gridy = 4;
				item7.add(item10, c);
			}

			add(item7, BorderLayout.CENTER);

			Handler hnd2 = new Handler();
			item3.addActionListener(hnd2);
			item4.addActionListener(hnd2);
			item8.addActionListener(hnd2);
			item9.addActionListener(hnd2);
			item10.addActionListener(hnd2);
			item11.addActionListener(hnd2);
		}
	}

	private class Handler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			path = new StringBuilder();

			if (event.getSource() == item1)
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Choose the input file");
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter txtType = new FileNameExtensionFilter(
						"text document (.txt)", "txt");
				chooser.addChoosableFileFilter(txtType);

				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
				{
					// The circuit is yet to be analyzed so it's not faield yet
					failed = false;

					path.append(chooser.getSelectedFile());

					TextInputReader tir = new TextInputReader(path.toString());

					circuit = new Circuit();
					circuit.createCicuit(tir);
					try
					{
						output = circuit.generateAllOutputs();
					} catch (Exception e)
					{
						e.printStackTrace();
					}

					simplifiedOutput = circuit.generateSimplifiedOutput();
					boolean are_equal;
					if (simplifiedOutput.length() == output.length())
					{
						are_equal = true;
					} else
					{
						are_equal = false;
					}
					SeconderyFrame sec = new SeconderyFrame(path, are_equal);

					sec.setResizable(false);
					sec.setVisible(true);

				} else
				{
					path.append("No Selection");
					JOptionPane.showMessageDialog(null, path);
				}

			} else if (event.getSource() == item2)
			{
				String cpal = new String();
				cpal = "Programmed and Revised By: \n\nAhmed Fathy Aly - Section 1\nAhmed Fathy Abdel Mageed - Section 1\nAhmed Fathy Hussain Fathy - Section 1\nAhmed Ra'fat Elsayed Al Alfy - Section 1\nMohamed Ahmed Anwer - Section 7\n\n2nd year Electrical - Logic Project\n� 2013-2014";
				JOptionPane.showMessageDialog(null, cpal);
			} else if (event.getSource() == item3)
			{
				JOptionPane.showMessageDialog(null, output);
			} else if (event.getSource() == item4)
			{
				JOptionPane.showMessageDialog(null, simplifiedOutput);
			} else if (event.getSource() == item8)
			{
				try
				{
					PrintWriter writer = new PrintWriter("Output.txt", "UTF-8");

					String[] out = output.split("\n");

					writer.println("Normal Output:");
					writer.println();

					for (int i = 0; i < out.length; i++)
					{
						writer.println(out[i]);
					}

					writer.close();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Output file was generated in application directory");
			}

			else if (event.getSource() == item9)
			{
				try
				{
					PrintWriter writer = new PrintWriter(
							"SimplifiedOutput.txt", "UTF-8");
					String[] simpOut = simplifiedOutput.split("\n");
					writer.println("Simplified Output:");
					writer.println();

					for (int i = 0; i < simpOut.length; i++)
					{
						writer.println(simpOut[i]);
					}
					writer.close();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				}
				JOptionPane
						.showMessageDialog(null,
								"Simplified Output file was generated in application directory");
			}

			else if (event.getSource() == item10)
			{
				try
				{
					circuit.drawCircuit(output, "output.pdf");
					JOptionPane
							.showMessageDialog(null,
									"Output state diagram file was generated in application directory");
				} catch (Exception e)
				{
				}
			}

			else if (event.getSource() == item11)
			{
				try
				{
					circuit.drawCircuit(simplifiedOutput,
							"Simplified_output.pdf");
					JOptionPane
							.showMessageDialog(null,
									"Simplified Output state diagram file was generated in application directory");
				} catch (Exception e)
				{
				}
			}

		}
	}

	public static void showError(String errorMessage)
	{
		if (failed == false)
		{
			JOptionPane.showMessageDialog(null, errorMessage);
			failed = true;
		}

	}
}