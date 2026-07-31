import 'package:injectable/injectable.dart';
import '../../../../core/usecase/usecase.dart';
import '../entities/inventory_item.dart';
import '../repositories/inventory_repository.dart';

@injectable
class SearchInventoryUseCase implements UseCase<List<InventoryItem>, String> {
  final InventoryRepository repository;

  SearchInventoryUseCase(this.repository);

  @override
  Future<List<InventoryItem>> call(String params) async {
    return await repository.searchInventory(params);
  }
}
