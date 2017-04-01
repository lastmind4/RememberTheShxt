import javax.inject.Inject

import filters.{LoggingFilter, SessionFilter}
import play.api.http.DefaultHttpFilters

class Filters @Inject()(loggingFilter: LoggingFilter, sessionFilter: SessionFilter) extends DefaultHttpFilters(loggingFilter, sessionFilter)