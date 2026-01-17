import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class BikeShop extends JFrame {

    private ArrayList<CartItem> cart = new ArrayList<>();
    
    private JPanel cartItemsPanel;
    private JLabel totalAmountLabel;

    // Dark Theme Colors
    private final Color BG_MAIN = new Color(30, 30, 30);
    private final Color BG_PANEL = new Color(50, 50, 50);
    private final Color ACCENT = new Color(0, 255, 200); 
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color BTN_RED = new Color(255, 80, 80);

    public BikeShop() {
        setTitle("BIKE SHOP - Ultimate Edition");
        setSize(1300, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_MAIN);

        // --- Header ---
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 40, 20));
        headerPanel.setBackground(BG_MAIN);
        
        JLabel titleLbl = new JLabel("BIKE SHOP SHOWROOM");
        titleLbl.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLbl.setForeground(ACCENT);
        headerPanel.add(titleLbl);
        add(headerPanel, BorderLayout.NORTH);

        // --- Products Grid ---
        Product[] products = {
            new Product("CFMOTO 300 NK", 342000.0, "./Images/bike1.jpg"),
            new Product("Honda CBR 150R", 530000.0, "./Images/bike2.png"),
            new Product("Hero Thriller 160V", 235000.0, "./Images/bike3.jpg"),
            new Product("Honda CB150X", 389000.0, "./Images/bike4.jpg")
        };

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBackground(BG_MAIN);
        gridPanel.setBorder(new EmptyBorder(10, 40, 40, 40));

        for (Product p : products) {
            gridPanel.add(createProductCard(p));
        }
        
        JPanel centerContainer = new JPanel(new GridBagLayout());
        centerContainer.setBackground(BG_MAIN);
        centerContainer.add(gridPanel);
        add(centerContainer, BorderLayout.CENTER);

        // --- Sidebar (Cart) ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(420, 0));
        sidebar.setBackground(BG_PANEL);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 2, 0, 0, Color.GRAY));

        JLabel cartHeader = new JLabel("YOUR CART", SwingConstants.CENTER);
        cartHeader.setFont(new Font("SansSerif", Font.BOLD, 22));
        cartHeader.setForeground(TEXT_WHITE);
        cartHeader.setBorder(new EmptyBorder(20, 0, 20, 0));
        sidebar.add(cartHeader, BorderLayout.NORTH);

        cartItemsPanel = new JPanel();
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));
        cartItemsPanel.setBackground(BG_PANEL);
        cartItemsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(cartItemsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PANEL);
        sidebar.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new GridLayout(3, 1, 10, 10));
        footer.setBackground(BG_PANEL);
        footer.setBorder(new EmptyBorder(20, 20, 30, 20));

        totalAmountLabel = new JLabel("Total: 0 TK", SwingConstants.CENTER);
        totalAmountLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        totalAmountLabel.setForeground(ACCENT);

        JButton payBtn = createButton("CONFIRM PAYMENT", ACCENT, Color.BLACK);
        payBtn.addActionListener(e -> processPayment());

        JButton clearBtn = createButton("CLEAR CART", BTN_RED, TEXT_WHITE);
        clearBtn.addActionListener(e -> {
            cart.clear();
            updateCart();
        });

        footer.add(totalAmountLabel);
        footer.add(payBtn);
        footer.add(clearBtn);
        sidebar.add(footer, BorderLayout.SOUTH);

        add(sidebar, BorderLayout.EAST);
    }

    private JPanel createProductCard(Product p) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_PANEL);
        card.setBorder(new LineBorder(Color.DARK_GRAY, 1));

        JLabel img = new JLabel();
        img.setAlignmentX(CENTER_ALIGNMENT);
        img.setPreferredSize(new Dimension(200, 140));
        try {
            ImageIcon icon = new ImageIcon(p.imgPath);
            Image i = icon.getImage().getScaledInstance(200, 140, Image.SCALE_SMOOTH);
            img.setIcon(new ImageIcon(i));
        } catch (Exception e) {
            img.setText("No Image");
            img.setForeground(Color.LIGHT_GRAY);
        }

        JLabel name = new JLabel(p.name);
        name.setFont(new Font("SansSerif", Font.BOLD, 16));
        name.setForeground(TEXT_WHITE);
        name.setAlignmentX(CENTER_ALIGNMENT);

        JLabel price = new JLabel(String.format("%.0f TK", p.price));
        price.setForeground(ACCENT);
        price.setAlignmentX(CENTER_ALIGNMENT);

        JButton btn = createButton("Add to Cart", BG_MAIN, TEXT_WHITE);
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setBorder(new LineBorder(ACCENT));
        btn.addActionListener(e -> addToCart(p));

        card.add(Box.createVerticalStrut(15));
        card.add(img);
        card.add(Box.createVerticalStrut(10));
        card.add(name);
        card.add(Box.createVerticalStrut(5));
        card.add(price);
        card.add(Box.createVerticalStrut(10));
        card.add(btn);
        card.add(Box.createVerticalStrut(15));
        return card;
    }

    private void addToCart(Product p) {
        boolean found = false;
        for (CartItem item : cart) {
            if (item.product.name.equals(p.name)) {
                item.quantity++;
                found = true;
                break;
            }
        }
        if (!found) {
            cart.add(new CartItem(p, 1));
        }
        updateCart();
    }

    private void updateCart() {
        cartItemsPanel.removeAll();
        double total = 0.0;

        if (cart.isEmpty()) {
            JLabel empty = new JLabel("Cart is empty");
            empty.setForeground(Color.GRAY);
            empty.setAlignmentX(CENTER_ALIGNMENT);
            cartItemsPanel.add(Box.createVerticalStrut(50));
            cartItemsPanel.add(empty);
        } else {
            for (int i = 0; i < cart.size(); i++) {
                final int idx = i;
                CartItem item = cart.get(i);
                double itemTotal = item.product.price * item.quantity;
                total += itemTotal;

                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(new Color(60, 60, 65));
                row.setMaximumSize(new Dimension(400, 80));
                row.setBorder(new EmptyBorder(10, 10, 10, 10));

                JLabel nameLbl = new JLabel("<html><font color='white'>" + item.product.name + "</font><br><font color='#aaaaaa'>" + String.format("%.0f", item.product.price) + " TK</font></html>");
                nameLbl.setPreferredSize(new Dimension(140, 50));

                JPanel ctrlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
                ctrlPanel.setOpaque(false);

                JButton minus = new JButton("-");
                styleMiniBtn(minus);
                minus.addActionListener(e -> {
                    if (item.quantity > 1) {
                        item.quantity--;
                        updateCart();
                    }
                });

                JLabel qty = new JLabel(" " + item.quantity + " ");
                qty.setForeground(TEXT_WHITE);
                qty.setFont(new Font("SansSerif", Font.BOLD, 14));

                JButton plus = new JButton("+");
                styleMiniBtn(plus);
                plus.addActionListener(e -> {
                    item.quantity++;
                    updateCart();
                });

                JButton del = new JButton("X");
                styleMiniBtn(del);
                del.setBackground(BTN_RED);
                del.addActionListener(e -> {
                    cart.remove(idx);
                    updateCart();
                });

                ctrlPanel.add(minus);
                ctrlPanel.add(qty);
                ctrlPanel.add(plus);
                ctrlPanel.add(Box.createHorizontalStrut(10));
                ctrlPanel.add(del);

                row.add(nameLbl, BorderLayout.WEST);
                row.add(ctrlPanel, BorderLayout.EAST);
                
                cartItemsPanel.add(row);
                cartItemsPanel.add(Box.createVerticalStrut(10));
            }
        }

        totalAmountLabel.setText("Total: " + String.format("%.0f", total) + " TK");
        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    private void processPayment() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty!");
            return;
        }

        int choice = JOptionPane.showConfirmDialog(this, 
            "Total Amount: " + totalAmountLabel.getText() + "\nAre you sure you want to confirm purchase?",
            "Confirm Payment", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Payment Successful!\nThank you.");
            cart.clear();
            updateCart();
        }
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void styleMiniBtn(JButton btn) {
        btn.setBackground(Color.DARK_GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Monospaced", Font.BOLD, 12));
        btn.setMargin(new Insets(2, 8, 2, 8));
        btn.setBorder(new LineBorder(Color.GRAY));
    }
}