import 'package:fflipy/viewmodels/invoice_view_model.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class InvoiceProvider extends StatelessWidget {
    final Widget child;

    const InvoiceProvider({Key? key, required this.child}) : super(key: key);

    @override
    Widget build(BuildContext context) {
        return ChangeNotifierProvider(
            create: (context) => InvoiceViewModel(),
            child: child,
        );
    }
}
