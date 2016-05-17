angular.module('newsfeed')

.filter('filterByTags', function() {
  return function(items, tags) {
    if (tags.length === 0) {
      return items;
    }
    var filtered = [];
    (items || []).forEach(function(item) {
      var matches = tags.some(function(tag) {
        return (item.tagData.toString().indexOf(tag.text) > -1);
      });
      if (matches) {
        filtered.push(item);
      }
    });
    return filtered;
  };
});