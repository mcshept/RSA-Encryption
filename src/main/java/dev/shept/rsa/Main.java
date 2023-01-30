package dev.shept.rsa;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.DarculaTheme;

import java.awt.EventQueue;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class Main {

    private JFrame frmFrame;
    private JTextField firstprime;
    private JTextField secondprime;
    private JTextField message;
    private JTextArea decryptedmsg;
    private JTextArea pubkeyE;
    private JTextArea privkeyD;
    private JTextArea encryptedmsg;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frmFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public Main() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        // Specify explicit theme.
        LafManager.install(new DarculaTheme());
        LafManager.setDecorationsEnabled(false);

        frmFrame = new JFrame();
        frmFrame.setTitle("RSA-Visualization");
        frmFrame.setBounds(100, 100, 450, 300);
        frmFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmFrame.getContentPane().setLayout(null);

        firstprime = new JTextField();
        firstprime.setText("Erste Primzahl");
        firstprime.setBounds(10, 11, 152, 20);
        frmFrame.getContentPane().add(firstprime);
        firstprime.setColumns(10);

        secondprime = new JTextField();
        secondprime.setText("Zweite Primzahl");
        secondprime.setBounds(272, 11, 152, 20);
        frmFrame.getContentPane().add(secondprime);
        secondprime.setColumns(10);

        message = new JTextField();
        message.setText("Zahl zum Verschl\u00FCsseln");
        message.setBounds(10, 76, 414, 20);
        frmFrame.getContentPane().add(message);
        message.setColumns(10);

        JButton submit = new JButton("Verschl\u00FCsseln");
        submit.setBounds(302, 107, 122, 23);
        submit.addActionListener(event -> {
            if (firstprime == null || secondprime == null || message == null) {
                JOptionPane.showMessageDialog(null, "Bitte gib in jedem Feld etwas ein.");
                return;
            }

            int p, q, n, z, d = 0, e, i, msg;

            try {
                // The number to be encrypted and decrypted
                msg = Integer.parseInt(message.getText());
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Bitte gib als Zahl zum Verschlüsseln nur eine Zahl an!");
                return;
            }
            double c;
            BigInteger msgback;

            try {
                // 1st prime number p
                p = Integer.parseInt(firstprime.getText());
                // 2nd prime number q
                q = Integer.parseInt(secondprime.getText());
            } catch (NumberFormatException exception) {
                JOptionPane.showMessageDialog(null, "Bitte gib als Primzahlen nur Zahlen an!");
                return;
            }

            n = p * q; // n is for the module
            z = (p - 1) * (q - 1);

            for (e = 2; e < z; e++) {

                // e is for public key exponent
                if (gcd(e, z) == 1) {
                    break;
                }
            }

            pubkeyE.setText("Öffentlicher Schlüssel: " + n + e);

            for (i = 0; i <= 9; i++) {
                int x = 1 + (i * z);

                // d is for private key exponent
                if (x % e == 0) {
                    d = x / e;
                    break;
                }
            }

            privkeyD.setText("Privater Schlüssel: " + n + d);

            // Encryption magic...
            c = (Math.pow(msg, e)) % n;
            encryptedmsg.setText("Verschlüsselte Nummer: " + c);

            // converting int value of n to BigInteger
            BigInteger N = BigInteger.valueOf(n);

            // converting float value of c to BigInteger
            BigInteger C = BigDecimal.valueOf(c).toBigInteger();
            msgback = (C.pow(d)).mod(N);
            decryptedmsg.setText("Entschlüsselte Nummer: " + msgback);

        });
        frmFrame.getContentPane().add(submit);

        privkeyD = new JTextArea();
        privkeyD.setEditable(false);
        privkeyD.setText("Private Key");
        privkeyD.setBounds(10, 147, 169, 42);
        frmFrame.getContentPane().add(privkeyD);

        pubkeyE = new JTextArea();
        pubkeyE.setEditable(false);
        pubkeyE.setText("Public Key");
        pubkeyE.setBounds(10, 200, 169, 42);
        frmFrame.getContentPane().add(pubkeyE);

        encryptedmsg = new JTextArea();
        encryptedmsg.setEditable(false);
        encryptedmsg.setBounds(189, 141, 235, 48);
        frmFrame.getContentPane().add(encryptedmsg);

        decryptedmsg = new JTextArea();
        decryptedmsg.setEditable(false);
        decryptedmsg.setBounds(189, 194, 235, 48);
        frmFrame.getContentPane().add(decryptedmsg);
    }

    static int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

}