package org.jsp.stocks.service.implementation;

import java.text.DecimalFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;              // ✅ ADDED - Required for API response
import java.util.Optional;
import java.util.Random;

import org.jsp.stocks.dto.AdminData;
import org.jsp.stocks.dto.Stock;
import org.jsp.stocks.dto.User;
import org.jsp.stocks.dto.UserStocksTransaction;
import org.jsp.stocks.repository.AdminDataRepository;
import org.jsp.stocks.repository.StockRepository;
import org.jsp.stocks.repository.UserRepository;
import org.jsp.stocks.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.client.RestTemplate;  

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class StockServiceImpl implements StockService {

    DecimalFormat format = new DecimalFormat("#0.00");

    @Autowired
    StockRepository stockRepository;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    JavaMailSender mailSender;
    
    @Autowired
    AdminDataRepository dataRepository;
    
    @Autowired
    private RestTemplate restTemplate;    // ✅ ADDED
    
    @Value("${platformPercentage}")
    double platformPercentage;

    @Value("${razor-pay.api.key}")
    String razorpayKey;

    @Value("${razor-pay.api.secret}")
    String razorpaySecret;

    @Value("${admin.email}")
    String adminEmail;

    @Value("${admin.password}")
    String adminPassword;

    @Value("${stock.api.key}")           // ✅ ADDED
    String stockapikey;

    int generateOtp() {
        return new Random().nextInt(900000) + 100000;
    }
    
    public void sendOtpEmail(String toEmail, int otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("NammaStocks - OTP Verification");
            message.setText("Hello,\n\nYour OTP for account verification is: " + otp + "\n\nThank you,\nNammaStocks Team");
            mailSender.send(message);
            System.out.println("✅ Email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
    
    @PostConstruct
    public void initAdminData() {
        if (dataRepository.findById(1).isEmpty()) {
            AdminData adminData = new AdminData();
            adminData.setId(1);
            adminData.setPlatformFeePercentage(0.04);
            adminData.setTotalPlatformFee(0.0);
            adminData.setTotalStocksBought(0.0);
            adminData.setTotalStocksSold(0.0);
            adminData.setTotalTransaction(0.0);
            dataRepository.save(adminData);
            System.out.println("✅ Default AdminData created with ID 1");
        }
    }

    public boolean updateStockFromAPI(Stock stock) {
        try {
            String ticker = stock.getTicker().toUpperCase().trim();
            System.out.println("Processing ticker: " + ticker);
            
            // Manual data for testing (No API call needed)
            // RELIANCE
            if (ticker.equals("RELIANCE") || ticker.equals("RELIANCE.BSE") || ticker.equals("RELIANCE.NS")) {
                stock.setCompanyName("Reliance Industries Ltd.");
                stock.setPrice(2500.00);
                stock.setQuantity(10000);
                stock.setChanges(1.5);
                System.out.println("✅ RELIANCE added");
            }
            // TCS
            else if (ticker.equals("TCS") || ticker.equals("TCS.BSE") || ticker.equals("TCS.NS")) {
                stock.setCompanyName("Tata Consultancy Services Ltd.");
                stock.setPrice(3800.00);
                stock.setQuantity(8000);
                stock.setChanges(2.0);
                System.out.println("✅ TCS added");
            }
            // INFOSYS
            else if (ticker.equals("INFY") || ticker.equals("INFY.BSE") || ticker.equals("INFY.NS")) {
                stock.setCompanyName("Infosys Ltd.");
                stock.setPrice(1500.00);
                stock.setQuantity(15000);
                stock.setChanges(1.2);
                System.out.println("✅ INFOSYS added");
            }
            // WIPRO
            else if (ticker.equals("WIPRO") || ticker.equals("WIPRO.BSE") || ticker.equals("WIPRO.NS")) {
                stock.setCompanyName("Wipro Ltd.");
                stock.setPrice(450.00);
                stock.setQuantity(20000);
                stock.setChanges(0.8);
                System.out.println("✅ WIPRO added");
            }
            // HDFC BANK
            else if (ticker.equals("HDFC") || ticker.equals("HDFCBANK") || ticker.equals("HDFCBANK.BSE") || ticker.equals("HDFC.BSE")) {
                stock.setCompanyName("HDFC Bank Ltd.");
                stock.setPrice(1600.00);
                stock.setQuantity(12000);
                stock.setChanges(1.0);
                System.out.println("✅ HDFC BANK added");
            }
            // ICICI BANK
            else if (ticker.equals("ICICI") || ticker.equals("ICICIBANK") || ticker.equals("ICICIBANK.BSE")) {
                stock.setCompanyName("ICICI Bank Ltd.");
                stock.setPrice(950.00);
                stock.setQuantity(18000);
                stock.setChanges(0.5);
                System.out.println("✅ ICICI BANK added");
            }
            // SBI
            else if (ticker.equals("SBI") || ticker.equals("SBIN") || ticker.equals("SBIN.BSE")) {
                stock.setCompanyName("State Bank of India");
                stock.setPrice(600.00);
                stock.setQuantity(25000);
                stock.setChanges(0.3);
                System.out.println("✅ SBI added");
            }
            // TATA MOTORS
            else if (ticker.equals("TATAMOTORS") || ticker.equals("TATAMOTORS.BSE")) {
                stock.setCompanyName("Tata Motors Ltd.");
                stock.setPrice(550.00);
                stock.setQuantity(22000);
                stock.setChanges(-0.5);
                System.out.println("✅ TATA MOTORS added");
            }
            // ITC
            else if (ticker.equals("ITC") || ticker.equals("ITC.BSE")) {
                stock.setCompanyName("ITC Limited");
                stock.setPrice(420.00);
                stock.setQuantity(30000);
                stock.setChanges(0.2);
                System.out.println("✅ ITC added");
            }
            // HINDALCO
            else if (ticker.equals("HINDALCO") || ticker.equals("HINDALCO.BSE")) {
                stock.setCompanyName("Hindalco Industries Ltd.");
                stock.setPrice(480.00);
                stock.setQuantity(15000);
                stock.setChanges(-0.3);
                System.out.println("✅ HINDALCO added");
            }
            // Default for any other ticker
            else {
                stock.setCompanyName(ticker);
                stock.setPrice(100.00);
                stock.setQuantity(5000);
                stock.setChanges(0.0);
                System.out.println("✅ Default data set for: " + ticker);
            }
            
            System.out.println("   Company: " + stock.getCompanyName());
            System.out.println("   Price: ₹" + stock.getPrice());
            System.out.println("   Quantity: " + stock.getQuantity());
            
            return true;
            
        } catch (Exception e) {
            System.err.println("Error setting stock data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public String register(User user, Model model) {
        model.addAttribute("user", user);
        return "register.html";
    }

    @Override
    public String register(User user, BindingResult result, HttpSession session) {

        if (user.getPassword() == null || user.getConfirmPassword() == null ||
                !user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error", "Password mismatch");
            return "register.html";
        }

        if (user.getDob() != null) {
            if (LocalDate.now().getYear() - user.getDob().getYear() < 18) {
                result.rejectValue("dob", "error", "Must be 18+");
                return "register.html";
            }
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error", "Email already exists");
            return "register.html";
        }

        if (userRepository.existsByMobile(user.getMobile())) {
            result.rejectValue("mobile", "error", "Mobile number already exists");
            return "register.html";
        }

        if (result.hasErrors()) {
            return "register.html";
        }

        user.setOtp(generateOtp());
        user.setPassword(user.getPassword());
        user.setVerified(false);
        user.setAmount(0.0);
        userRepository.save(user);

        System.out.println("=================================");
        System.out.println("OTP for " + user.getEmail() + " is: " + user.getOtp());
        System.out.println("=================================");

        sendOtpEmail(user.getEmail(), user.getOtp());

        session.setAttribute("pass", "OTP sent to your email");
        return "redirect:/otp/" + user.getId();
    }

    @Override
    public String verifyOtp(int id, int otp, HttpSession session) {

        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            session.setAttribute("fail", "User not found");
            return "redirect:/register";
        }

        User user = optionalUser.get();

        if (user.getOtp() == otp) {
            user.setVerified(true);
            user.setOtp(0);
            userRepository.save(user);

            session.setAttribute("pass", "Account created successfully!");
            return "redirect:/login";
        } else {
            session.setAttribute("fail", "Invalid OTP");
            return "redirect:/otp/" + id;
        }
    }
    
    public void removeMessage() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                HttpSession session = request.getSession(false);
                if (session != null) {
                    // Clear both success and failure messages
                    session.removeAttribute("pass");
                    session.removeAttribute("fail");
                    System.out.println("✅ Messages cleared from session");
                }
            }
        } catch (Exception e) {
            System.err.println("Error clearing messages: " + e.getMessage());
        }
    }

    @Override
    public String login(String email, String password, HttpSession session) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        
        session.removeAttribute("user");
        session.removeAttribute("admin");
        
        // Admin login
        if (email.equals("admin@gmail.com") && password.equals("admin123")) {
            System.out.println("✅ Admin login successful");
            session.setAttribute("admin", "admin");
            session.setAttribute("pass", "Login Success - Welcome Admin");
            return "redirect:/";
        }
        
        // User login
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            System.out.println("❌ User not found: " + email);
            session.setAttribute("fail", "Invalid Email");
            return "redirect:/login";
        }
        
        User user = userOptional.get();
        System.out.println("✅ User found: " + user.getName());
        
        if (password.equals(user.getPassword())) {
            System.out.println("✅ Password match!");
            if (user.isVerified()) {
                session.setAttribute("user", user);
                session.setAttribute("pass", "Login Success, Welcome " + user.getName());
                return "redirect:/";
            } else {
                System.out.println("❌ User not verified!");
                session.setAttribute("fail", "Please verify your account first. Check OTP.");
                return "redirect:/login";
            }
        } else {
            System.out.println("❌ Password mismatch!");
            session.setAttribute("fail", "Invalid Password");
            return "redirect:/login";
        }
    }

    @Override
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @Override
    public String addStock(HttpSession session) {
        if (session.getAttribute("admin") != null) {
            return "add-stock.html";
        } else {
            session.setAttribute("fail", "Admin access required");
            return "redirect:/login";
        }
    }

    @Override
    public String addStock(HttpSession session, Stock stock) {
        if (session.getAttribute("admin") != null) {
            try {
                boolean flag = updateStockFromAPI(stock);
                
                if (flag) {
                    if (stockRepository.existsById(stock.getTicker())) {
                        session.setAttribute("fail", "Stock Already Present for " + stock.getTicker());
                    } else {
                        if (stock.getPrice() > 0) {
                            stockRepository.save(stock);
                            session.setAttribute("pass", "Stock Added Success for " + stock.getCompanyName());
                        } else {
                            session.setAttribute("fail", "Stock data not found for " + stock.getTicker() + ". Check ticker symbol.");
                        }
                    }
                } else {
                    session.setAttribute("fail", "Stock Not Found for " + stock.getTicker());
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                session.setAttribute("fail", "Error adding stock");
            }
            return "redirect:/manage-stocks";
        } else {
            session.setAttribute("fail", "Admin access required");
            return "redirect:/login";
        }
    }

    @Override
    public String fetchStocks(HttpSession session, Model model) {
        if (session.getAttribute("admin") != null) {
            List<Stock> stocks = stockRepository.findAll();
            model.addAttribute("stocks", stocks);
            return "admin-view-stocks.html";
        } else {
            session.setAttribute("fail", "Admin access required");
            return "redirect:/login";
        }
    }

    @Override
    public String deleteStock(String ticker, HttpSession session) {
        if (session.getAttribute("admin") != null) {
            stockRepository.deleteById(ticker);
            session.setAttribute("pass", "Stock deleted");
            return "redirect:/manage-stocks";
        } else {
            session.setAttribute("fail", "Admin access required");
            return "redirect:/login";
        }
    }

    @Override
    public String viewStocks(HttpSession session, Model model, String company) {
        if (session.getAttribute("user") != null) {
            List<Stock> stocks;
            if (company == null || company.isEmpty()) {
                stocks = stockRepository.findAll();
            } else {
                stocks = stockRepository.findByCompanyNameLike("%" + company + "%");
            }
            
            if (stocks.isEmpty()) {
                session.setAttribute("fail", "No stocks available");
                model.addAttribute("stocks", new ArrayList<>());
            } else {
                model.addAttribute("stocks", stocks);
            }
            return "user-view-stocks.html";
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }

    @Override
    public String viewStock(HttpSession session, Model model, String ticker) {
        if (session.getAttribute("user") != null) {
            Optional<Stock> stock = stockRepository.findById(ticker);
            if (stock.isPresent()) {
                model.addAttribute("stock", stock.get());
                return "view-stock.html";
            } else {
                session.setAttribute("fail", "Stock not found");
                return "redirect:/view-stocks";
            }
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }

    @Override
    public String viewWallet(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            model.addAttribute("amount", user.getAmount());
            System.out.println("Wallet balance: " + user.getAmount());
            return "wallet.html";
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }

    @Override
    public String rechargeWallet(double amount, HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            user.setAmount(user.getAmount() + amount);
            userRepository.save(user);
            session.setAttribute("user", user);
            session.setAttribute("pass", "₹" + amount + " added to wallet successfully!");
            return "redirect:/wallet";
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }

    @Override
    public String paymentSuccess(double amount, HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            user.setAmount(user.getAmount() + amount);
            userRepository.save(user);
            session.setAttribute("user", user);
            session.setAttribute("pass", "Wallet recharged successfully!");
            return "redirect:/wallet";
        } else {
            return "redirect:/login";
        }
    }

    @Override
    public String buyStock(String ticker, double quantity, HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            Optional<Stock> stockOpt = stockRepository.findById(ticker);
            if (stockOpt.isPresent()) {
                Stock stock = stockOpt.get();
                if (quantity <= stock.getQuantity()) {
                    double totalPrice = stock.getPrice() * quantity;
                    User user = (User) session.getAttribute("user");
                    model.addAttribute("totalPrice", totalPrice);
                    model.addAttribute("platformFee", totalPrice * platformPercentage);
                    model.addAttribute("totalWithFee", totalPrice + (totalPrice * platformPercentage));
                    model.addAttribute("quantity", quantity);
                    model.addAttribute("ticker", ticker);
                    model.addAttribute("price", stock.getPrice());
                    model.addAttribute("wallet", user.getAmount());
                    return "confirm-buy.html";
                } else {
                    session.setAttribute("fail", "Not enough quantity available");
                    return "redirect:/view-stocks";
                }
            } else {
                session.setAttribute("fail", "Stock not found");
                return "redirect:/view-stocks";
            }
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }
    
    @Override
    public String confirmPurchase(HttpSession session, String ticker, double quantity, double price) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            double totalPrice = price * quantity;
            double platformFee = totalPrice * platformPercentage;  // Calculate platform fee
            
            if (user.getAmount() >= totalPrice + platformFee) {
                Optional<Stock> stockOpt = stockRepository.findById(ticker);
                if (stockOpt.isPresent()) {
                    Stock stock = stockOpt.get();
                    
                    // Update stock quantity
                    stock.setQuantity(stock.getQuantity() - quantity);
                    stockRepository.save(stock);
                    
                    // Deduct total amount including platform fee
                    user.setAmount(user.getAmount() - (totalPrice + platformFee));
                    
                    // Add to portfolio
                    List<UserStocksTransaction> transactions = user.getTransactions();
                    if (transactions == null) {
                        transactions = new ArrayList<>();
                    }
                    
                    // Check if already owns this stock
                    boolean found = false;
                    for (UserStocksTransaction trans : transactions) {
                        if (trans.getStock_ticker().equals(ticker)) {
                            trans.setQuantity(trans.getQuantity() + quantity);
                            trans.setPrice((trans.getPrice() + price) / 2); // Average price
                            found = true;
                            break;
                        }
                    }
                    
                    if (!found) {
                        UserStocksTransaction transaction = new UserStocksTransaction();
                        transaction.setStock_ticker(ticker);
                        transaction.setQuantity(quantity);
                        transaction.setPrice(price);
                        transactions.add(transaction);
                    }
                    user.setTransactions(transactions);
                    userRepository.save(user);
                    
                    // ✅ UPDATE ADMIN DATA (Platform fees)
                    Optional<AdminData> adminDataOpt = dataRepository.findById(1);
                    AdminData adminData;
                    if (adminDataOpt.isPresent()) {
                        adminData = adminDataOpt.get();
                    } else {
                        adminData = new AdminData();
                        adminData.setId(1);
                        adminData.setPlatformFeePercentage(platformPercentage);
                    }
                    
                    adminData.setTotalPlatformFee(adminData.getTotalPlatformFee() + platformFee);
                    adminData.setTotalStocksBought(adminData.getTotalStocksBought() + quantity);
                    adminData.setTotalTransaction(adminData.getTotalTransaction() + totalPrice);
                    dataRepository.save(adminData);
                    
                    session.setAttribute("user", user);
                    session.setAttribute("pass", "Stock purchased successfully! Platform fee: ₹" + platformFee);
                    return "redirect:/portfolio";
                }
            } else {
                session.setAttribute("fail", "Insufficient wallet balance (including platform fee)");
                return "redirect:/wallet";
            }
        }
        return "redirect:/login";
    }

    @Override
    public String viewOverview(HttpSession session, Model model) {
        if (session.getAttribute("admin") != null) {
            Optional<AdminData> data = dataRepository.findById(1);
            if (data.isPresent()) {
                model.addAttribute("data", data.get());
                return "overview.html";
            } else {
                session.setAttribute("fail", "No data available");
                return "redirect:/";
            }
        } else {
            session.setAttribute("fail", "Admin access required");
            return "redirect:/login";
        }
    }

    @Override
    public String viewPortfolio(HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            List<UserStocksTransaction> transactions = user.getTransactions();
            
            if (transactions == null || transactions.isEmpty()) {
                session.setAttribute("fail", "Your portfolio is empty");
                model.addAttribute("transactions", new ArrayList<>());
                return "portfolio.html";
            }
            
            double totalInvested = 0;
            double currentValue = 0;
            
            for (UserStocksTransaction transaction : transactions) {
                totalInvested += transaction.getPrice() * transaction.getQuantity();
                
                Optional<Stock> stockOpt = stockRepository.findById(transaction.getStock_ticker());
                if (stockOpt.isPresent()) {
                    currentValue += stockOpt.get().getPrice() * transaction.getQuantity();
                } else {
                    currentValue += transaction.getPrice() * transaction.getQuantity();
                }
            }
            
            model.addAttribute("totalInvested", totalInvested);
            model.addAttribute("currentValue", currentValue);
            model.addAttribute("transactions", transactions);
            return "portfolio.html";
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }
    
    @Override
    public String viewSell(String ticker, HttpSession session, Model model) {
        if (session.getAttribute("user") != null) {
            Optional<Stock> stock = stockRepository.findById(ticker);
            if (stock.isPresent()) {
                model.addAttribute("stock", stock.get());
                return "enter-quantity.html";
            } else {
                session.setAttribute("fail", "Stock not found");
                return "redirect:/portfolio";
            }
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }

    @Override
    public String sellStocks(double quantity, String ticker, HttpSession session) {
        if (session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            List<UserStocksTransaction> transactions = user.getTransactions();
            
            if (transactions == null || transactions.isEmpty()) {
                session.setAttribute("fail", "You don't own any stocks to sell");
                return "redirect:/portfolio";
            }
            
            // Find the stock in user's portfolio
            UserStocksTransaction foundTransaction = null;
            for (UserStocksTransaction transaction : transactions) {
                if (transaction.getStock_ticker().equals(ticker)) {
                    foundTransaction = transaction;
                    break;
                }
            }
            
            if (foundTransaction == null) {
                session.setAttribute("fail", "You don't own this stock");
                return "redirect:/portfolio";
            }
            
            if (quantity > foundTransaction.getQuantity()) {
                session.setAttribute("fail", "Not enough quantity to sell. You only have " + foundTransaction.getQuantity() + " shares");
                return "redirect:/portfolio";
            }
            
            Optional<Stock> stockOpt = stockRepository.findById(ticker);
            if (stockOpt.isEmpty()) {
                session.setAttribute("fail", "Stock not found in market");
                return "redirect:/portfolio";
            }
            
            Stock stock = stockOpt.get();
            double sellValue = stock.getPrice() * quantity;
            double platformFee = sellValue * platformPercentage;  // Platform fee on sell
            double amountToAdd = sellValue - platformFee;
            
            // Update transaction quantity
            foundTransaction.setQuantity(foundTransaction.getQuantity() - quantity);
            if (foundTransaction.getQuantity() <= 0) {
                transactions.remove(foundTransaction);
            }
            
            // Update user's wallet balance (after deducting platform fee)
            user.setAmount(user.getAmount() + amountToAdd);
            user.setTransactions(transactions);
            userRepository.save(user);
            
            // Update stock quantity in market
            stock.setQuantity(stock.getQuantity() + quantity);
            stockRepository.save(stock);
            
            // ✅ UPDATE ADMIN DATA for selling
            Optional<AdminData> adminDataOpt = dataRepository.findById(1);
            AdminData adminData;
            if (adminDataOpt.isPresent()) {
                adminData = adminDataOpt.get();
            } else {
                adminData = new AdminData();
                adminData.setId(1);
                adminData.setPlatformFeePercentage(platformPercentage);
            }
            
            adminData.setTotalPlatformFee(adminData.getTotalPlatformFee() + platformFee);
            adminData.setTotalStocksSold(adminData.getTotalStocksSold() + quantity);
            dataRepository.save(adminData);
            
            session.setAttribute("user", user);
            session.setAttribute("pass", "Successfully sold " + quantity + " shares of " + ticker + " for ₹" + sellValue + " (Platform fee: ₹" + platformFee + ")");
            return "redirect:/portfolio";
            
        } else {
            session.setAttribute("fail", "Please login first");
            return "redirect:/login";
        }
    }
}