package ru.mcko.registry.datatables;

import lombok.Data;

import java.util.*;
import java.util.regex.Pattern;

@Data
public class DataTablesInput {
  /**
   * Format: <code>searchPanes.$attribute.0</code> (<code>searchPanes[$attribute][0]</code> without jquery.spring-friendly.js)
   *
   * @see <a href="https://github.com/DataTables/SearchPanes/blob/5e6d3229cd90594cc67d6d266321f1c922fc9231/src/searchPanes.ts#L119-L137">source</a>
   */
  private static final Pattern SEARCH_PANES_REGEX = Pattern.compile("^searchPanes\\.(\\w+)\\.\\d+$");

  /**
   * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side
   * processing requests are drawn in sequence by DataTables (Ajax requests are asynchronous and
   * thus can return out of sequence). This is used as part of the draw return parameter (see
   * below).
   */
  private Integer draw = 1;

  /**
   * Paging first record indicator. This is the start point in the current data set (0 index based -
   * i.e. 0 is the first record).
   */
  private Integer start = 0;

  /**
   * Number of records that the table can display in the current draw. It is expected that the
   * number of records returned will be equal to this number, unless the server has fewer records to
   * return. Note that this can be -1 to indicate that all records should be returned (although that
   * negates any benefits of server-side processing!)
   */
  private Integer length = 10;

  /**
   * Global search parameter.
   */
  private Search search = new Search();

  /**
   * Order parameter
   */
  private List<Order> order = new ArrayList<>();

  /**
   * Per-column search parameter
   */
  private List<Column> columns = new ArrayList<>();

  /**
   * Input for the <a href="https://datatables.net/extensions/searchpanes/">SearchPanes extension</a>
   */
  private Map<String, Set<String>> searchPanes;


}
