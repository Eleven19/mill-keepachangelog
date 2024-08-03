module Example.Model.Term exposing (Term(..), TermDto, fromDto, toDto)

type Term
    = IriRef String
    | Literal String
    | BlankNode String

type alias TermDto =
    { iriRef : Maybe String
    , literal : Maybe String
    , blankNode : Maybe String
    }

fromDto : TermDto -> Maybe Term
fromDto dto =
    case (dto.iriRef, dto.literal, dto.blankNode) of
        ( Just iriRef, Nothing, Nothing ) -> Just (IriRef iriRef)
        ( Nothing, Just literal, Nothing ) -> Just (Literal literal)
        ( Nothing, Nothing, Just blankNode ) -> Just (BlankNode blankNode)
        _ -> Nothing

toDto : Term -> TermDto
toDto term =
    case term of
        IriRef iriRef -> { iriRef = Just iriRef, literal = Nothing, blankNode = Nothing }
        Literal literal -> { iriRef = Nothing, literal = Just literal, blankNode = Nothing }
        BlankNode blankNode -> { iriRef = Nothing, literal = Nothing, blankNode = Just blankNode }