import 'package:flutter/material.dart';

class ZakatCalculatorPage extends StatefulWidget {
  const ZakatCalculatorPage({super.key});

  @override
  State<ZakatCalculatorPage> createState() => _ZakatCalculatorPageState();
}

class _ZakatCalculatorPageState extends State<ZakatCalculatorPage> {
  final _cashController = TextEditingController();
  final _goldController = TextEditingController();
  final _silverController = TextEditingController();
  final _savingsController = TextEditingController();
  final _debtsController = TextEditingController();

  double _totalWealth = 0;
  double _zakatPayable = 0;

  void _calculateZakat() {
    double cash = double.tryParse(_cashController.text) ?? 0;
    double gold = double.tryParse(_goldController.text) ?? 0;
    double silver = double.tryParse(_silverController.text) ?? 0;
    double savings = double.tryParse(_savingsController.text) ?? 0;
    double debts = double.tryParse(_debtsController.text) ?? 0;

    setState(() {
      _totalWealth = (cash + gold + silver + savings) - debts;
      if (_totalWealth > 0) {
        _zakatPayable = _totalWealth * 0.025; // 2.5% Zakat
      } else {
        _totalWealth = 0;
        _zakatPayable = 0;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Zakat Calculator'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const Text(
              'Enter your wealth values to calculate Zakat (2.5%)',
              style: TextStyle(color: Colors.grey),
            ),
            const SizedBox(height: 16),
            _buildInputField('Cash at hand/bank', _cashController),
            _buildInputField('Value of Gold', _goldController),
            _buildInputField('Value of Silver', _silverController),
            _buildInputField('Investments/Savings', _savingsController),
            _buildInputField('Debts/Liabilities (Subtracts)', _debtsController),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: _calculateZakat,
              style: ElevatedButton.styleFrom(
                padding: const EdgeInsets.symmetric(vertical: 16),
                backgroundColor: Theme.of(context).colorScheme.primary,
                foregroundColor: Colors.white,
              ),
              child: const Text('Calculate Zakat', style: TextStyle(fontSize: 18)),
            ),
            const SizedBox(height: 32),
            if (_totalWealth > 0)
              Container(
                padding: const EdgeInsets.all(20),
                decoration: BoxDecoration(
                  color: Theme.of(context).colorScheme.primary.withOpacity(0.1),
                  borderRadius: BorderRadius.circular(16),
                  border: Border.all(color: Theme.of(context).colorScheme.primary),
                ),
                child: Column(
                  children: [
                    const Text(
                      'Total Eligible Wealth',
                      style: TextStyle(fontSize: 16, color: Colors.grey),
                    ),
                    Text(
                      _totalWealth.toStringAsFixed(2),
                      style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                    ),
                    const Divider(height: 32),
                    const Text(
                      'Total Zakat Payable (2.5%)',
                      style: TextStyle(fontSize: 16, color: Colors.grey),
                    ),
                    Text(
                      _zakatPayable.toStringAsFixed(2),
                      style: TextStyle(
                        fontSize: 32,
                        fontWeight: FontWeight.bold,
                        color: Theme.of(context).colorScheme.primary,
                      ),
                    ),
                  ],
                ),
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildInputField(String label, TextEditingController controller) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 16.0),
      child: TextField(
        controller: controller,
        keyboardType: const TextInputType.numberWithOptions(decimal: true),
        decoration: InputDecoration(
          labelText: label,
          border: OutlineInputBorder(borderRadius: BorderRadius.circular(12)),
          prefixIcon: const Icon(Icons.attach_money),
        ),
      ),
    );
  }
}
